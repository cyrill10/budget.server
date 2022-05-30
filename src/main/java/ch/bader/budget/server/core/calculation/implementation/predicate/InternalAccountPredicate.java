package ch.bader.budget.server.core.calculation.implementation.predicate;

import ch.bader.budget.server.adapter.sql.entity.RealAccountDboSql;

import java.util.function.Predicate;

public class InternalAccountPredicate implements Predicate<RealAccountDboSql> {

    @Override
    public boolean test(RealAccountDboSql a) {
        return a.getAccountType().isInternalAccount();
    }

}
