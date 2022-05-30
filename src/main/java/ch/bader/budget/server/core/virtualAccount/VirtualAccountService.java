package ch.bader.budget.server.core.virtualAccount;

import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.repository.VirtualAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VirtualAccountService {

    @Autowired
    private VirtualAccountRepository virtualAccountRepository;
    

    public VirtualAccount addVirtualAccount(VirtualAccount account) {
        return virtualAccountRepository.save(account);
    }

    public VirtualAccount updateVirtualAccount(VirtualAccount account) {
        return virtualAccountRepository.updateVirtualAccount(account);
    }

    public VirtualAccount getAccountById(Integer id) {
        return virtualAccountRepository.getAccountById(id);
    }

    public List<VirtualAccount> getAllVirtualAccounts() {
        return virtualAccountRepository.getAllVirtualAccountsWithTheirUnderlyingAccount();
    }

    public List<VirtualAccount> getAllVirtualAccountsForRealAccount(Integer realAccountId) {
        return virtualAccountRepository.getAllVirtualAccountsForRealAccount(realAccountId);
    }
}
