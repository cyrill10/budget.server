package ch.bader.budget.server.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ch.bader.budget.server.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	@Query("SELECT t FROM Transaction t WHERE t.date < ?1 AND t.date > ?2")
	List<Transaction> findAllTransactionsInInterval(LocalDate from, LocalDate to);

}
