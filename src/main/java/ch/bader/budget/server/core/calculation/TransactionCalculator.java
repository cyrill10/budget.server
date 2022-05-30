package ch.bader.budget.server.core.calculation;

import ch.bader.budget.server.core.calculation.implementation.function.BudgetedAmountFunction;
import ch.bader.budget.server.core.calculation.implementation.function.EffectiveAmountFunction;
import ch.bader.budget.server.core.calculation.implementation.predicate.TransactionBeforePredicate;
import ch.bader.budget.server.core.calculation.implementation.predicate.TransactionInMonthPredicate;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.TransactionElement;
import ch.bader.budget.server.domain.VirtualAccount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TransactionCalculator {

    private static final EffectiveAmountFunction effectiveAmountFunction = new EffectiveAmountFunction();

    private static final BudgetedAmountFunction budgetedAmountFunction = new BudgetedAmountFunction();

    public static List<TransactionElement> getTransactionsForMonth(List<Transaction> transactions,
                                                                   VirtualAccount virtualAccount,
                                                                   LocalDate from) {
        Balance in = new Balance(BigDecimal.ZERO, BigDecimal.ZERO);
        Balance out = new Balance(BigDecimal.ZERO, BigDecimal.ZERO);

        Predicate<Transaction> beforePredicate = new TransactionBeforePredicate(from);

        Balance accountBalance =
                VirtualAccountCalculator.getBalanceAt(virtualAccount,
                        transactions.stream().filter(beforePredicate).collect(Collectors.toList()),
                        effectiveAmountFunction,
                        budgetedAmountFunction, from);

        TransactionElement before = new TransactionElement("Before", accountBalance.getEffective(),
                accountBalance.getBudgeted(), "0");

        Predicate<Transaction> duringPredicate = new TransactionInMonthPredicate(from);

        LinkedList<TransactionElement> transactionElements = transactions.stream()
                                                                         .distinct()
                                                                         .filter(duringPredicate)
                                                                         .map(t -> createTransactionElement(t,
                                                                                 virtualAccount,
                                                                                 accountBalance))
                                                                         // This peek is ugly and keeps track of how much money went in and out in total
                                                                         .peek(t -> {
                                                                             if (t.getAmount()
                                                                                  .compareTo(BigDecimal.ZERO) >= 0) {
                                                                                 in.add(t.getAmount(), BigDecimal.ZERO);
                                                                             } else {
                                                                                 out.subtract(t.getAmount(),
                                                                                         BigDecimal.ZERO);
                                                                             }
                                                                             if (t.getBudgetedAmount()
                                                                                  .compareTo(BigDecimal.ZERO) >= 0) {
                                                                                 in.add(BigDecimal.ZERO,
                                                                                         t.getBudgetedAmount());
                                                                             } else {
                                                                                 out.subtract(BigDecimal.ZERO,
                                                                                         t.getBudgetedAmount());
                                                                             }
                                                                         })
                                                                         .collect(Collectors.toCollection(LinkedList::new));
        TransactionElement after = new TransactionElement("After",
                accountBalance.getEffective(),
                accountBalance.getBudgeted(), String.valueOf(Integer.MAX_VALUE - 1));
        TransactionElement in_out = new TransactionElement(in.getEffective(), out.getEffective(), in.getBudgeted(),
                out.getBudgeted());
        transactionElements.push(before);
        transactionElements.add(after);
        transactionElements.add(in_out);
        return transactionElements;
    }

    public static TransactionElement createTransactionElement(Transaction transaction,
                                                              VirtualAccount virtualAccount,
                                                              Balance accountBalance) {
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal budgetedAmount = BigDecimal.ZERO;
        if (transaction.getDebitedAccount().equals(virtualAccount)) {
            amount = transaction.getEffectiveAmount();
            budgetedAmount = transaction.getBudgetedAmount();
            accountBalance.add(effectiveAmountFunction.apply(transaction, virtualAccount.isPrebudgetedAccount()),
                    budgetedAmountFunction.apply(transaction, virtualAccount.isPrebudgetedAccount()));
        }
        if (transaction.getCreditedAccount().equals(virtualAccount)) {
            amount = BigDecimal.ZERO.subtract(transaction.getEffectiveAmount());
            budgetedAmount = BigDecimal.ZERO.subtract(transaction.getBudgetedAmount());

            accountBalance.subtract(effectiveAmountFunction.apply(transaction,
                    virtualAccount.isPrebudgetedAccount()), budgetedAmountFunction.apply(transaction,
                    virtualAccount.isPrebudgetedAccount()));
        }

        return new TransactionElement(transaction, amount, budgetedAmount, accountBalance.getEffective(),
                accountBalance.getBudgeted());
    }

    public static List<TransactionElement> getTransactionsForMonth(List<Transaction> transactions,
                                                                   List<VirtualAccount> virtualAccounts,
                                                                   LocalDate from) {

        Predicate<Transaction> beforePredicate = new TransactionBeforePredicate(from);

        Balance accountBalance =
                VirtualAccountCalculator.getBalanceAt(virtualAccounts,
                        transactions.stream().filter(beforePredicate).collect(Collectors.toList()),
                        effectiveAmountFunction,
                        budgetedAmountFunction, from);

        TransactionElement before = new TransactionElement("Before", accountBalance.getEffective(),
                accountBalance.getBudgeted(), "0");

        Predicate<Transaction> duringPredicate = new TransactionInMonthPredicate(from);

        LinkedList<TransactionElement> transactionElements = transactions.stream()
                                                                         .distinct()
                                                                         .filter(duringPredicate)
                                                                         .map(t -> createTransactionElement(t,
                                                                                 virtualAccounts,
                                                                                 accountBalance))
                                                                         .collect(Collectors.toCollection(LinkedList::new));
        TransactionElement after = new TransactionElement("After",
                accountBalance.getEffective(),
                accountBalance.getBudgeted(), String.valueOf(Integer.MAX_VALUE - 1));
        transactionElements.push(before);
        transactionElements.add(after);
        return transactionElements;
    }

    public static TransactionElement createTransactionElement(Transaction transaction,
                                                              List<VirtualAccount> virtualAccounts,
                                                              Balance accountBalance) {
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal budgetedAmount = BigDecimal.ZERO;
        if (virtualAccounts.contains(transaction.getDebitedAccount())) {
            amount = transaction.getEffectiveAmount();
            budgetedAmount = transaction.getBudgetedAmount();
            accountBalance.add(transaction.getEffectiveAmount(), transaction.getBudgetedAmount());
        }
        if (virtualAccounts.contains(transaction.getCreditedAccount())) {
            amount = BigDecimal.ZERO.subtract(transaction.getEffectiveAmount());
            budgetedAmount = BigDecimal.ZERO.subtract(transaction.getBudgetedAmount());
            accountBalance.subtract(transaction.getEffectiveAmount(), transaction.getBudgetedAmount());
        }

        return new TransactionElement(transaction, amount, budgetedAmount, accountBalance.getEffective(),
                accountBalance.getBudgeted());
    }


}
