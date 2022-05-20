package ch.bader.budget.server.calculation.implementation.function;

import ch.bader.budget.server.entity.Transaction;

import java.math.BigDecimal;
import java.util.function.Function;

public class EffectiveAmountFunction implements Function<Transaction, Number> {

    @Override
    public Number apply(Transaction t) {
        if (t.getEffectiveAmount().equals(BigDecimal.ZERO) && t.isNotPrebudgetedAccount()) {
            return t.getBudgetedAmount();
        }
        return t.getEffectiveAmount();
    }
}
