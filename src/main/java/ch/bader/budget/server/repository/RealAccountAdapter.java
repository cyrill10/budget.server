package ch.bader.budget.server.repository;

import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.type.AccountType;

import java.util.List;
import java.util.Map;

public interface RealAccountAdapter {
    RealAccount save(RealAccount realAccount);

    RealAccount getAccountById(String id);

    Map<RealAccount, List<VirtualAccount>> getAccountMap();

    RealAccount updateRealAccount(RealAccount account);

    List<RealAccount> findAll();

    List<RealAccount> getAccountsByTyp(AccountType accountType);
}
