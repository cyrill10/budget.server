package ch.bader.budget.server.core.calculation.implementation.predicate;

import ch.bader.budget.server.domain.Transaction;

import java.time.LocalDate;
import java.util.function.Predicate;

public class TransactionBeforePredicate implements Predicate<Transaction> {

    private final LocalDate date;

    public TransactionBeforePredicate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean test(Transaction t) {
        return t.getDate().isBefore(date);
    }

}
