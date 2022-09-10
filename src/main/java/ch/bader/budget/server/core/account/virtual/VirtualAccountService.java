package ch.bader.budget.server.core.account.virtual;

import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.repository.VirtualAccountAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VirtualAccountService {

    @Autowired
    @Qualifier("virtualAccountMongo")
    private VirtualAccountAdapter virtualAccountAdapter;
    

    public VirtualAccount updateVirtualAccount(VirtualAccount account) {
        return virtualAccountAdapter.updateVirtualAccount(account);
    }

    public VirtualAccount getAccountById(String id) {
        return virtualAccountAdapter.getAccountById(id);
    }

    public List<VirtualAccount> getAllVirtualAccounts() {
        return virtualAccountAdapter.getAllVirtualAccountsWithTheirUnderlyingAccount();
    }
}
