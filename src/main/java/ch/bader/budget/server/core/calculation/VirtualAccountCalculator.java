package ch.bader.budget.server.core.calculation;

import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.VirtualAccount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class VirtualAccountCalculator {

    public static Balance getBalanceAt(VirtualAccount account, List<Transaction> allTransactions,
                                       BiFunction<Transaction, Boolean, BigDecimal> effectiveBalanceTypeFunction,
                                       BiFunction<Transaction, Boolean, BigDecimal> budgetedBalanceTypeFunction,
                                       LocalDate untilDate) {

        if (account.getUnderlyingAccount().getAccountType().isAlienAccount()) {
            return new Balance(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        BiPredicate<Transaction, LocalDate> transactionPredicate = account.getTransactionPredicate();

        Balance balance = new Balance(account.getBalance(), account.getBalance());

        allTransactions.stream().distinct().filter(t -> transactionPredicate.test(t, untilDate)).forEach(t -> {
            BigDecimal effectiveBalanceChange = effectiveBalanceTypeFunction.apply(t, account.isPrebudgetedAccount());
            BigDecimal budgetedBalanceChange = budgetedBalanceTypeFunction.apply(t, account.isPrebudgetedAccount());
            if (t.getCreditedAccount().equals(account)) {
                balance.subtract(effectiveBalanceChange, budgetedBalanceChange);
            }
            if (t.getDebitedAccount().equals(account)) {
                balance.add(effectiveBalanceChange, budgetedBalanceChange);
            }
        });

        return balance;
    }

    public static Balance getBalanceAt(List<VirtualAccount> accounts, List<Transaction> allTransactions,
                                       BiFunction<Transaction, Boolean, BigDecimal> effectiveBalanceTypeFunction,
                                       BiFunction<Transaction, Boolean, BigDecimal> budgetedBalanceTypeFunction,
                                       LocalDate untilDate) {
        Balance balance = new Balance(BigDecimal.ZERO, BigDecimal.ZERO);
        accounts.stream()
                .distinct()
                .forEach(account -> balance.add(getBalanceAt(account,
                        allTransactions,
                        effectiveBalanceTypeFunction,
                        budgetedBalanceTypeFunction, untilDate)));
        return balance;

    }
}
