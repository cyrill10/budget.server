package ch.bader.budget.server.calculation.implementation.predicate;

import java.util.function.Predicate;

import ch.bader.budget.server.entity.VirtualAccount;

public class OverviewVirtualAccountPredicate implements Predicate<VirtualAccount> {

	@Override
	public boolean test(VirtualAccount a) {
		return a.getUnderlyingAccount().getAccountType().isOverviewAccount();
	}

}
