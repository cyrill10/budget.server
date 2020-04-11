package ch.bader.budget.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.bader.budget.server.entity.RealAccount;

public interface RealAccountRepository extends JpaRepository<RealAccount, Integer> {

}
