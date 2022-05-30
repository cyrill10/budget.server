package ch.bader.budget.server.core.calculation;

import ch.bader.budget.server.core.calculation.implementation.function.BudgetedAmountFunction;
import ch.bader.budget.server.core.calculation.implementation.function.EffectiveAmountFunction;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.VirtualAccount;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class OverviewData {

    private final BigDecimal balanceAfter;

    private final BigDecimal budgetedBalanceAfter;

    private final BigDecimal projection;

    private final BigDecimal budgetedProjection;

    protected OverviewData(VirtualAccount virtualAccount, List<Transaction> transactions,
                           List<Transaction> transactionsTillEndOfYear, LocalDate untilDate) {

        Balance balance = VirtualAccountCalculator.getBalanceAt(virtualAccount,
                transactions,
                new EffectiveAmountFunction(),
                new BudgetedAmountFunction(), untilDate);

        balanceAfter = balance.getEffective();
        budgetedBalanceAfter = balance.getBudgeted();

        if (virtualAccount.isPrebudgetedAccount()) {
            this.projection = budgetedBalanceAfter.subtract(balanceAfter);
            this.budgetedProjection = null;
        } else {
            Balance projectedBalance = VirtualAccountCalculator.getBalanceAt(virtualAccount,
                    Stream.of(transactions, transactionsTillEndOfYear)
                          .flatMap(Collection::stream)
                          .collect(Collectors.toList()),
                    new EffectiveAmountFunction(),
                    new BudgetedAmountFunction(), untilDate);
            this.projection = projectedBalance.getEffective();

            this.budgetedProjection = projectedBalance.getBudgeted();
        }
    }
}
