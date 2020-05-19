package ch.bader.budget.server.calculation.implementation.function;

import java.time.LocalDate;
import java.util.function.Function;

import ch.bader.budget.server.entity.Transaction;

public class BudgetedAmountFunction implements Function<Transaction, Number> {

	@Override
	public Number apply(Transaction t) {
		LocalDate firstDayOfThisMonth = LocalDate.now().withDayOfMonth(1);
		if (firstDayOfThisMonth.isAfter(t.getDate())) {
			return t.getEffectiveAmount();
		}
		return t.getBudgetedAmount();
	}

}
