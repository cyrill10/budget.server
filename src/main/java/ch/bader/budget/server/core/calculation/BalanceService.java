package ch.bader.budget.server.core.calculation;

import ch.bader.budget.server.core.calculation.implementation.function.BudgetedAmountFunction;
import ch.bader.budget.server.core.calculation.implementation.function.EffectiveAmountFunction;
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
                                      LocalDate date) {
        Balance balance =
                virtualAccounts.stream()
                        .map(VirtualAccount::getInitialBalance)
                        .reduce(new Balance(), Balance::add);

        transactions.stream()
                .distinct()
                .filter(t -> t.getDate().isBefore(date))
                .forEach(t -> {
                    BigDecimal effectiveBalanceChange =
                            new EffectiveAmountFunction().apply(t);
                    BigDecimal budgetedBalanceChange =
                            new BudgetedAmountFunction().apply(t);
                    if (virtualAccounts.contains(t.getCreditedAccount())) {
                        balance.subtract(effectiveBalanceChange, budgetedBalanceChange);
                    }
                    if (virtualAccounts.contains(t.getDebitedAccount())) {
                        balance.add(effectiveBalanceChange, budgetedBalanceChange);
                    }
                });

        return balance;
    }
}
