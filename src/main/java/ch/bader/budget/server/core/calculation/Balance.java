package ch.bader.budget.server.core.calculation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class Balance {

    private BigDecimal effective;
    private BigDecimal budgeted;

    public Balance() {
        this(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public Balance(Balance copyBalance) {
        this(copyBalance.getEffective(), copyBalance.getBudgeted());
    }


    public void add(BigDecimal effective, BigDecimal budgeted) {
        this.effective = this.effective.add(effective);
        this.budgeted = this.budgeted.add(budgeted);
    }

    public void subtract(BigDecimal effective, BigDecimal budgeted) {
        this.effective = this.effective.subtract(effective);
        this.budgeted = this.budgeted.subtract(budgeted);
    }

    public Balance add(Balance balance) {
        this.effective = this.effective.add(balance.effective);
        if (this.budgeted != null && balance.budgeted != null) {
            this.budgeted = this.budgeted.add(balance.budgeted);
        }
        return this;
    }
}
