package ch.bader.budget.server.domain;

import ch.bader.budget.server.type.AccountType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RealAccount implements Comparable<RealAccount> {
    private String id;
    private String name;
    private AccountType accountType;

    public boolean isPrebudgetedAccount() {
        return this.getAccountType().isPrebudgetedAccount();
    }

    @Override
    public int compareTo(RealAccount o) {
        int compareType = this.getAccountType().getValue().compareTo(o.getAccountType().getValue());
        if (compareType == 0) {
            return this.getName().compareTo(o.getName());
        }
        return compareType;
    }

    public boolean isAlienAccount() {
        return this.getAccountType().isAlienAccount();
    }
}
