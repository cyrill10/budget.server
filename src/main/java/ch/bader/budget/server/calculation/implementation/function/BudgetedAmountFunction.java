package ch.bader.budget.server.calculation.implementation.function;

import java.util.function.Function;

import ch.bader.budget.server.entity.Transaction;

public class BudgetedAmountFunction implements Function<Transaction, Number> {

	@Override
	public Number apply(Transaction t) {
		return t.getBudgetedAmount();
	}

}
