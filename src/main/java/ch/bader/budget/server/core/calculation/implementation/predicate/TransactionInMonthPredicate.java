package ch.bader.budget.server.core.calculation.implementation.predicate;

import ch.bader.budget.server.domain.Transaction;

import java.time.LocalDate;
import java.util.function.Predicate;

public class TransactionInMonthPredicate implements Predicate<Transaction> {

    private final LocalDate date;

    public TransactionInMonthPredicate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean test(Transaction t) {
        return t.getDate().isAfter(date.minusDays(1)) && t.getDate().isBefore(date.plusMonths(1));
    }

}
