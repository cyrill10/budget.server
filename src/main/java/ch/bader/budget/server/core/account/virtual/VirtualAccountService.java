package ch.bader.budget.server.core.account.virtual;

import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.repository.VirtualAccountAdapter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VirtualAccountService {

    private final VirtualAccountAdapter virtualAccountAdapter;

    public VirtualAccountService(VirtualAccountAdapter virtualAccountAdapter) {
        this.virtualAccountAdapter = virtualAccountAdapter;
    }


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
