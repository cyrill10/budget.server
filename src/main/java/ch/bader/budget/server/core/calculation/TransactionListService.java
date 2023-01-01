package ch.bader.budget.server.core.calculation;

import ch.bader.budget.server.core.calculation.implementation.function.BudgetedAmountFunction;
import ch.bader.budget.server.core.calculation.implementation.function.EffectiveAmountFunction;
import ch.bader.budget.server.core.calculation.implementation.predicate.TransactionInMonthPredicate;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.TransactionListElement;
import ch.bader.budget.server.domain.VirtualAccount;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class TransactionListService {

    private static final EffectiveAmountFunction effectiveAmountFunction =
            new EffectiveAmountFunction();

    private static final BudgetedAmountFunction budgetedAmountFunction =
            new BudgetedAmountFunction();

    private final BalanceService balanceService;

    public TransactionListService(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    public List<TransactionListElement> getTransactionListElementsForMonth(
            List<Transaction> transactions, List<VirtualAccount> virtualAccounts,
            LocalDate firstOfMonth) {
        Balance balance =
                balanceService.calculateBalanceAt(transactions, virtualAccounts, firstOfMonth);

        Balance transactionsWithAlien = new Balance(BigDecimal.ZERO, BigDecimal.ZERO);

        TransactionInMonthPredicate transactionInMonthPredicate =
                new TransactionInMonthPredicate(firstOfMonth);

        LinkedList<TransactionListElement> transactionList = new LinkedList<>();

        transactionList.add(new TransactionListElement("Before", balance.getEffective(),
                balance.getBudgeted(), "0"));

        transactions.stream().filter(transactionInMonthPredicate).forEach(transaction -> {
            TransactionListElement transactionListElement =
                    createTransactionElement(transaction, virtualAccounts, balance);
            updateAlienTransaction(transactionsWithAlien, transaction, transactionListElement);
            transactionList.add(transactionListElement);

        });

        transactionList.add(new TransactionListElement("After", balance.getEffective(),
                balance.getBudgeted(), String.valueOf(
                Integer.MAX_VALUE - 1)));
        if (virtualAccounts.size() == 1) {
            transactionList.add(
                    new TransactionListElement(transactionsWithAlien.getEffective(),
                            transactionsWithAlien.getEffective()));
        }
        return transactionList;
    }

    private void updateAlienTransaction(Balance transactionWithAlien, Transaction transaction,
                                        TransactionListElement transactionListElement) {
        if (isTransactionWithAlienAccount(transaction)) {
            transactionWithAlien.add(transactionListElement.getEffectiveAmount(),
                    transactionListElement.getBudgetedAmount());
        }

    }

    private boolean isTransactionWithAlienAccount(Transaction transaction) {
        return transaction.getDebitedAccount().isAlienAccount() ||
                transaction.getCreditedAccount().isAlienAccount() ||
                transaction.getDebitedAccount().isPrebudgetedAccount() ||
                transaction.getCreditedAccount().isPrebudgetedAccount();
    }


    public TransactionListElement createTransactionElement(Transaction transaction,
                                                           List<VirtualAccount> virtualAccounts,
                                                           Balance accountBalance) {
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal budgetedAmount = BigDecimal.ZERO;
        if (virtualAccounts.contains(transaction.getDebitedAccount())) {
            amount = transaction.getEffectiveAmount();
            budgetedAmount = transaction.getBudgetedAmount();
            accountBalance.add(effectiveAmountFunction.apply(transaction),
                    budgetedAmountFunction.apply(transaction));
        }
        if (virtualAccounts.contains(transaction.getCreditedAccount())) {
            amount = BigDecimal.ZERO.subtract(transaction.getEffectiveAmount());
            budgetedAmount = BigDecimal.ZERO.subtract(transaction.getBudgetedAmount());

            accountBalance.subtract(effectiveAmountFunction.apply(transaction),
                    budgetedAmountFunction.apply(transaction));
        }

        return new TransactionListElement(transaction, amount, budgetedAmount,
                accountBalance.getEffective(), accountBalance.getBudgeted());
    }
}
