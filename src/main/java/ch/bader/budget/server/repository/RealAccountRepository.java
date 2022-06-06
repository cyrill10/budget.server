package ch.bader.budget.server.repository;

import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.VirtualAccount;

import java.util.List;
import java.util.Map;

public interface RealAccountRepository {
    RealAccount save(RealAccount realAccount);

    RealAccount getAccountById(Integer id);

    Map<RealAccount, List<VirtualAccount>> getAccountMap();

    RealAccount updateRealAccount(RealAccount account);
}
