package ch.bader.budget.server.calculation.implementation.predicate;

import java.time.LocalDate;
import java.util.function.Predicate;

import ch.bader.budget.server.entity.Transaction;

public class TransactionInMonthPredicate implements Predicate<Transaction> {

	private LocalDate date;

	public TransactionInMonthPredicate(LocalDate date) {
		this.date = date;
	}

	@Override
	public boolean test(Transaction t) {
		return t.getDate().isAfter(date.minusDays(1)) && t.getDate().isBefore(date.plusMonths(1));
	}

}
