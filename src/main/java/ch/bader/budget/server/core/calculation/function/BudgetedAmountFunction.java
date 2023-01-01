package ch.bader.budget.server.core.calculation.function;

import ch.bader.budget.server.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Function;

@NoArgsConstructor
@AllArgsConstructor
public class BudgetedAmountFunction implements Function<Transaction, BigDecimal> {

    private boolean isForPrebudgetedAccount;

    @Override
    public BigDecimal apply(Transaction t) {
        if (isForPrebudgetedAccount) {
            return t.getBudgetedAmount();
        }
        LocalDate firstDayOfThisMonth = LocalDate.now().withDayOfMonth(1);
        if (firstDayOfThisMonth.isAfter(t.getDate())) {
            return new EffectiveAmountFunction().apply(t);
        }
        return t.getBudgetedAmount();
    }
}
