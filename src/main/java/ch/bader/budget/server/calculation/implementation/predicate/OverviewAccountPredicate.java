package ch.bader.budget.server.calculation.implementation.predicate;

import java.util.function.Predicate;

import ch.bader.budget.server.entity.RealAccount;

public class OverviewAccountPredicate implements Predicate<RealAccount> {

	@Override
	public boolean test(RealAccount a) {
		return a.getAccountType().isOverviewAccount();
	}

}
