package ch.bader.budget.server.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiPredicate;

@Data
@Builder
public class VirtualAccount implements Comparable<VirtualAccount> {
    private String id;
    private String name;
    private BigDecimal balance;
    private Boolean isDeleted;
    private RealAccount underlyingAccount;

    public boolean isPrebudgetedAccount() {
        return this.getUnderlyingAccount().isPrebudgetedAccount();
    }

    public boolean isDeleted() {
        return Boolean.TRUE.equals(isDeleted);
    }

    private static BiPredicate<Transaction, LocalDate> noFilter = (transaction, date) -> true;

    private static BiPredicate<Transaction, LocalDate> onlyLastMonthFilter = (transaction, date) -> {
        LocalDate firstOfLastMonth = date.minusMonths(1);
        return !transaction.getDate().isBefore(firstOfLastMonth);
    };

    @Override
    public int compareTo(VirtualAccount o) {
        return this.getName().compareTo(o.getName());
    }

    public BiPredicate<Transaction, LocalDate> getTransactionPredicate() {
        if (this.isPrebudgetedAccount()) {
            return onlyLastMonthFilter;
        }
        return noFilter;
    }
}
