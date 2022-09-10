package ch.bader.budget.server.domain;

import ch.bader.budget.server.core.calculation.OverviewData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class OverviewElement {

    private String name;
    private BigDecimal balanceAfter;
    private BigDecimal budgetedBalanceAfter;
    private BigDecimal projection;
    private BigDecimal budgetedProjection;
    private boolean isRealAccount;
    private String id;

    public OverviewElement(VirtualAccount account, OverviewData data) {
        this.name = account.getName();
        this.balanceAfter = data.getBalanceAfter();
        this.budgetedBalanceAfter = data.getBudgetedBalanceAfter();
        this.projection = data.getProjection();
        this.budgetedProjection = data.getBudgetedProjection();
        this.isRealAccount = false;
        this.id = account.getId();
    }
}