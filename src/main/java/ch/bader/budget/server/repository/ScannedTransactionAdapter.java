package ch.bader.budget.server.repository;

import ch.bader.budget.server.domain.ScannedTransaction;

import java.time.YearMonth;
import java.util.List;

public interface ScannedTransactionAdapter {
    List<ScannedTransaction> saveAll(List<ScannedTransaction> transactionList);

    List<ScannedTransaction> getTransactionsForYearMonth(YearMonth yearMonth);

    List<ScannedTransaction> findAllById(List<String> transactionIds);
}
