package ch.bader.budget.server.core.realAccount;

import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.repository.RealAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RealAccountService {

    @Autowired
    private RealAccountRepository realAccountRepository;

    public RealAccount addRealAccount(RealAccount account) {
        return realAccountRepository.save(account);
    }

    public RealAccount getAccountById(Integer id) {
        return realAccountRepository.getAccountById(id);
    }

    public Map<RealAccount, List<VirtualAccount>> getAccountMap() {
        return realAccountRepository.getAccountMap();
    }

    public RealAccount updateRealAccount(RealAccount account) {
        return realAccountRepository.updateRealAccount(account);
    }
}
