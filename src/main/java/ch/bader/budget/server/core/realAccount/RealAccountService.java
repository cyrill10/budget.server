package ch.bader.budget.server.core.realAccount;

import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.repository.RealAccountAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RealAccountService {

    @Autowired
    @Qualifier("realAccountMongo")
    private RealAccountAdapter realAccountMongoAdapter;

    public RealAccount addRealAccount(RealAccount account) {
        return realAccountMongoAdapter.save(account);
    }

    public RealAccount getAccountById(String id) {
        return realAccountMongoAdapter.getAccountById(id);
    }

    public Map<RealAccount, List<VirtualAccount>> getAccountMap() {
        return realAccountMongoAdapter.getAccountMap();
    }

    public RealAccount updateRealAccount(RealAccount account) {
        return realAccountMongoAdapter.updateRealAccount(account);
    }
}
