package ch.bader.budget.server.core.calculation.predicate;

import ch.bader.budget.server.domain.RealAccount;

import java.util.function.Predicate;

public class OverviewRealAccountPredicate implements Predicate<RealAccount> {

    @Override
    public boolean test(RealAccount a) {
        return a.getAccountType().isOverviewAccount();
    }

}
