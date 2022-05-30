package ch.bader.budget.server.adapter.sql.repository;

import ch.bader.budget.server.adapter.sql.entity.ClosingProcessDboSql;
import ch.bader.budget.server.adapter.sql.entity.ScannedTransactionDboSql;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScannedTransactionJpaRepository extends JpaRepository<ScannedTransactionDboSql, Integer> {

    List<ScannedTransactionDboSql> findAllByClosingProcessOrderByDateAsc(ClosingProcessDboSql closingProcess);

}
