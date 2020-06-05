package ch.bader.budget.server.calculation.implementation.function;

import java.util.function.Function;

import ch.bader.budget.server.entity.Transaction;

public class EffectiveAmountFunction implements Function<Transaction, Number> {

	@Override
	public Number apply(Transaction t) {
		if (t.getEffectiveAmount() == 0 && t.isNotPrebudgetedAccount()) {
			return t.getBudgetedAmount();
		}
		return t.getEffectiveAmount();
	}
}
