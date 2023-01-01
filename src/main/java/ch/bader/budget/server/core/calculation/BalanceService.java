package ch.bader.budget.server.core.calculation;

import ch.bader.budget.server.core.calculation.function.BudgetedAmountFunction;
import ch.bader.budget.server.core.calculation.function.EffectiveAmountFunction;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.VirtualAccount;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BalanceService {
    public Balance calculateBalanceAt(List<Transaction> transactions,
                                      List<VirtualAccount> virtualAccounts,
                                      RealAccount underlyingAccount,
                                      LocalDate date) {

        if (underlyingAccount.isPrebudgetedAccount()) {
            return new Balance();
        }

        Balance balance =
                virtualAccounts.stream()
                        .map(VirtualAccount::getInitialBalance)
                        .reduce(new Balance(), Balance::add);

        addTransactionsToBalance(transactions, virtualAccounts, underlyingAccount,
                LocalDate.MIN, date,
                balance);

        return balance;
    }


    public Balance calculateBalanceWithinRange(Balance initialBalance,
                                               List<Transaction> transactions,
                                               List<VirtualAccount> virtualAccounts,
                                               RealAccount underlyingAccount,
                                               LocalDate minDate, LocalDate maxDate) {
        Balance newBalance = new Balance(initialBalance);
        addTransactionsToBalance(transactions, virtualAccounts, underlyingAccount,
                minDate, maxDate, newBalance);
        return newBalance;
    }

    private void addTransactionsToBalance(List<Transaction> transactions,
                                          List<VirtualAccount> virtualAccounts,
                                          RealAccount underlyingAccount, LocalDate lowDate,
                                          LocalDate highDate,
                                          Balance balance) {

        transactions.stream()
                .distinct()
                .filter(t -> virtualAccounts.contains(t.getCreditedAccount()) ||
                        virtualAccounts.contains(t.getDebitedAccount()))
                .filter(t -> t.getDate().isBefore(highDate))
                .filter(t -> !t.getDate().isBefore(lowDate))
                .forEach(t -> {
                    BigDecimal effectiveBalanceChange =
                            new EffectiveAmountFunction().apply(t);
                    BigDecimal budgetedBalanceChange =
                            new BudgetedAmountFunction(
                                    underlyingAccount.isPrebudgetedAccount()).apply(t);
                    if (virtualAccounts.contains(t.getCreditedAccount())) {
                        balance.subtract(effectiveBalanceChange, budgetedBalanceChange);
                    }
                    if (virtualAccounts.contains(t.getDebitedAccount())) {
                        balance.add(effectiveBalanceChange, budgetedBalanceChange);
                    }
                });
    }
}
