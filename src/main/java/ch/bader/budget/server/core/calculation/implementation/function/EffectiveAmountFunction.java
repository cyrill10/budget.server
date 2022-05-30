package ch.bader.budget.server.core.calculation.implementation.function;

import ch.bader.budget.server.domain.Transaction;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public class EffectiveAmountFunction implements BiFunction<Transaction, Boolean, BigDecimal> {

    @Override
    public BigDecimal apply(Transaction t, Boolean isPrebudgetedAmount) {
        if (t.getEffectiveAmount().equals(BigDecimal.ZERO) && !isPrebudgetedAmount) {
            return t.getBudgetedAmount();
        }
        return t.getEffectiveAmount();
    }
}
