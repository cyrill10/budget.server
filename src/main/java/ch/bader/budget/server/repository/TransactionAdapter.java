package ch.bader.budget.server.repository;

import ch.bader.budget.server.domain.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionAdapter {
    Transaction createTransaction(Transaction transaction);

    Transaction updateTransaction(Transaction transaction);

    void deleteTransaction(String transactionId);

    void saveTransactions(List<Transaction> transactionList);

    Transaction getTransactionById(String id);

    List<Transaction> getAllTransactions(LocalDate date);

    List<Transaction> getAllTransactionsUntilDate(LocalDate unitlExclusive);

    List<Transaction> getAllTransactionInInterval(LocalDate fromInclusive, LocalDate toExclusive);

    List<Transaction> getAllTransactionsForVirtualAccountUntilDate(String accountId, LocalDate unitlExclusive);

    List<Transaction> getAllTransactionsForVirtualAccountsUntilDate(List<String> accountIds, LocalDate unitlExclusive);
}
