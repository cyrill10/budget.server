package ch.bader.budget.server.core.calculation.function;

import ch.bader.budget.server.domain.Transaction;

import java.math.BigDecimal;
import java.util.function.Function;

public class EffectiveAmountFunction implements Function<Transaction, BigDecimal> {

    @Override
    public BigDecimal apply(Transaction t) {
        if (t.getEffectiveAmount().compareTo(BigDecimal.ZERO) == 0 &&
                !isTransactionPrebudgeted(t)) {
            return t.getBudgetedAmount();
        }
        return t.getEffectiveAmount();
    }

    private boolean isTransactionPrebudgeted(Transaction t) {
        return t.getCreditedAccount().isPrebudgetedAccount() ||
                t.getDebitedAccount().isPrebudgetedAccount();
    }
}
