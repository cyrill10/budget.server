package ch.bader.budget.server.calculation.implementation.predicate;

import java.time.LocalDate;
import java.util.function.Predicate;

import ch.bader.budget.server.entity.Transaction;

public class TransactionBeforePredicate implements Predicate<Transaction> {

	private LocalDate date;

	public TransactionBeforePredicate(LocalDate date) {
		this.date = date;
	}

	@Override
	public boolean test(Transaction t) {
		return t.getDate().isBefore(date);
	}

}
