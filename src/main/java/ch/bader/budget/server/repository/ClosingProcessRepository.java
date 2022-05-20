package ch.bader.budget.server.repository;

import ch.bader.budget.server.entity.ClosingProcess;
import ch.bader.budget.server.entity.RealAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClosingProcessRepository extends JpaRepository<ClosingProcess, Integer> {

    ClosingProcess findClosingProcessByYearAndMonth(Integer year, Integer month);

}
