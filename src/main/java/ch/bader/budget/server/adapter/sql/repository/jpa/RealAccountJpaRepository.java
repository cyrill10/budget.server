package ch.bader.budget.server.adapter.sql.repository.jpa;

import ch.bader.budget.server.adapter.sql.entity.RealAccountDboSql;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealAccountJpaRepository extends JpaRepository<RealAccountDboSql, Integer> {

}
