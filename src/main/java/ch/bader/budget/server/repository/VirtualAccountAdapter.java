package ch.bader.budget.server.repository;

import ch.bader.budget.server.domain.VirtualAccount;

import java.util.List;

public interface VirtualAccountAdapter {
    VirtualAccount save(VirtualAccount virtualAccount);

    VirtualAccount updateVirtualAccount(VirtualAccount account);

    VirtualAccount getAccountById(String id);

    List<VirtualAccount> getAllVirtualAccountsWithTheirUnderlyingAccount();

    List<VirtualAccount> getAllVirtualAccountsForRealAccount(String realAccountId);
}
