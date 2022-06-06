package ch.bader.budget.server.adapter.sql.repository.jpa;

import ch.bader.budget.server.adapter.sql.entity.TransactionDboSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<TransactionDboSql, Integer> {

    @Query("SELECT t FROM TransactionDboSql t WHERE t.date >= ?1 AND t.date < ?2 AND (t.creditedAccount.id = ?3 OR t.debitedAccount.id = ?3)")
    List<TransactionDboSql> findAllTransactionsInIntervalForVirtualAccount(LocalDate from, LocalDate to, int accountId);

    @Query("SELECT t FROM TransactionDboSql t WHERE t.date >= ?1 AND t.date < ?2 AND (t.creditedAccount.id in ?3 OR t.debitedAccount.id in ?3)")
    List<TransactionDboSql> findAllTransactionsInIntervalForVirtualAccounts(LocalDate from, LocalDate to,
                                                                            List<Integer> accountIds);

    @Query("SELECT t FROM TransactionDboSql t WHERE t.date >= ?1 AND t.date < ?2")
    List<TransactionDboSql> findAllTransactionsInInterval(LocalDate from, LocalDate to);

    @Query("SELECT t FROM TransactionDboSql t WHERE t.date >= ?1 AND t.date < ?2 AND (t.creditedAccount.underlyingAccount.id = ?3 OR t.debitedAccount.underlyingAccount.id = ?3)")
    List<TransactionDboSql> findAllTransactionsInIntervalForRealAccount(LocalDate from, LocalDate to, int accountId);

    @Query("SELECT t FROM TransactionDboSql t WHERE t.date >= ?1 AND t.date < ?2 AND t.creditedAccount.id = ?3")
    List<TransactionDboSql> findAllCreditedTransactionsInIntervalForVirtualAccount(LocalDate from, LocalDate to,
                                                                                   int accountId);


}
