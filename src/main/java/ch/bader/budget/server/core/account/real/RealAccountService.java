package ch.bader.budget.server.core.account.real;

import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.repository.RealAccountAdapter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RealAccountService {

    private final RealAccountAdapter realAccountAdapter;

    public RealAccountService(RealAccountAdapter realAccountAdapter) {
        this.realAccountAdapter = realAccountAdapter;
    }

    public RealAccount addRealAccount(RealAccount account) {
        return realAccountAdapter.save(account);
    }

    public RealAccount getAccountById(String id) {
        return realAccountAdapter.getAccountById(id);
    }

    public Map<RealAccount, List<VirtualAccount>> getAccountMap() {
        return realAccountAdapter.getAccountMap();
    }

    public RealAccount updateRealAccount(RealAccount account) {
        return realAccountAdapter.updateRealAccount(account);
    }
}
