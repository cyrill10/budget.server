package ch.bader.budget.server.adapter.sql.repository;

import ch.bader.budget.server.adapter.sql.entity.ClosingProcessDboSql;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClosingProcessJpaRepository extends JpaRepository<ClosingProcessDboSql, Integer> {

    ClosingProcessDboSql findClosingProcessByYearAndMonth(Integer year, Integer month);

}
