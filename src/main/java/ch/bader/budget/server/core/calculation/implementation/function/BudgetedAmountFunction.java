package ch.bader.budget.server.core.calculation.implementation.function;

import ch.bader.budget.server.domain.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Function;

public class BudgetedAmountFunction implements Function<Transaction, BigDecimal> {

    @Override
    public BigDecimal apply(Transaction t) {
        LocalDate firstDayOfThisMonth = LocalDate.now().withDayOfMonth(1);
        if (firstDayOfThisMonth.isAfter(t.getDate())) {
            return t.getEffectiveAmount();
        }
        return t.getBudgetedAmount();
    }
}
