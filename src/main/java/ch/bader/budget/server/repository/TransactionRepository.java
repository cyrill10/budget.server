package ch.bader.budget.server.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ch.bader.budget.server.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	@Query("SELECT t FROM Transaction t WHERE t.date >= ?1 AND t.date < ?2 AND (t.creditedAccount.id = ?3 OR t.debitedAccount.id = ?3)")
	List<Transaction> findAllTransactionsInIntervalForVirtualAccount(LocalDate from, LocalDate to, int accountId);

	@Query("SELECT t FROM Transaction t WHERE t.date >= ?1 AND t.date < ?2")
	List<Transaction> findAllTransactionsInInterval(LocalDate from, LocalDate to);

	@Query("SELECT t FROM Transaction t WHERE t.date >= ?1 AND t.date < ?2 AND (t.creditedAccount.underlyingAccount.id = ?3 OR t.debitedAccount.underlyingAccount.id = ?3)")
	List<Transaction> findAllTransactionsInIntervalForRealAccount(LocalDate from, LocalDate to, int accountId);

	@Query("SELECT t FROM Transaction t WHERE t.date >= ?1 AND t.date < ?2 AND t.creditedAccount.id = ?3")
	List<Transaction> findAllCreditedTransactionsInIntervalForVirtualAccount(LocalDate from, LocalDate to,
			int accountId);

}
