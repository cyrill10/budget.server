package ch.bader.budget.server.core.calculation;

import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.VirtualAccount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class VirtualAccountCalculator {

    private VirtualAccountCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static Balance getBalanceAt(VirtualAccount account, List<Transaction> allTransactions,
                                       Function<Transaction, BigDecimal> effectiveBalanceTypeFunction,
                                       Function<Transaction, BigDecimal> budgetedBalanceTypeFunction,
                                       LocalDate untilDate) {

        if (account.getUnderlyingAccount().getAccountType().isAlienAccount()) {
            return new Balance(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        BiPredicate<Transaction, LocalDate> transactionPredicate = account.getTransactionPredicate();

        Balance balance = new Balance(account.getBalance(), account.getBalance());

        allTransactions.stream().distinct().filter(t -> transactionPredicate.test(t, untilDate)).forEach(t -> {
            BigDecimal effectiveBalanceChange = effectiveBalanceTypeFunction.apply(t);
            BigDecimal budgetedBalanceChange = budgetedBalanceTypeFunction.apply(t);
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
                                       Function<Transaction, BigDecimal> effectiveBalanceTypeFunction,
                                       Function<Transaction, BigDecimal> budgetedBalanceTypeFunction,
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
