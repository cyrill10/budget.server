package ch.bader.budget.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.bader.budget.server.entity.VirtualAccount;

public interface VirtualAccountRepository extends JpaRepository<VirtualAccount, Integer> {

}
