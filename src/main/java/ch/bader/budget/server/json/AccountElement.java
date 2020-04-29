package ch.bader.budget.server.json;

import java.util.List;

import ch.bader.budget.server.entity.RealAccount;
import ch.bader.budget.server.entity.VirtualAccount;

public class AccountElement {

	private RealAccount realAccount;

	private List<VirtualAccount> virtualAccounts;

	public AccountElement(RealAccount realAccount, List<VirtualAccount> virtualAccounts) {
		super();
		this.realAccount = realAccount;
		this.virtualAccounts = virtualAccounts;
	}

	public RealAccount getRealAccount() {
		return realAccount;
	}

	public List<VirtualAccount> getVirtualAccounts() {
		return virtualAccounts;
	}

}
