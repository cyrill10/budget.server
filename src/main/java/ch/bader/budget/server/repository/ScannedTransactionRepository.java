package ch.bader.budget.server.repository;

import ch.bader.budget.server.entity.ClosingProcess;
import ch.bader.budget.server.entity.ScannedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScannedTransactionRepository extends JpaRepository<ScannedTransaction, Integer> {

    List<ScannedTransaction> findAllByClosingProcessOrderByDateAsc(ClosingProcess closingProcess);

}
