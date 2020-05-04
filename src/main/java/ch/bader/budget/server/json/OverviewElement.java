package ch.bader.budget.server.json;

import ch.bader.budget.server.calculation.VirtaulAccountMonthData;
import ch.bader.budget.server.entity.VirtualAccount;

public class OverviewElement {

	private String name;
	private Number balanceBefore;
	private Number balanceAfter;
	private Number budgetedBalanceBefore;
	private Number budgetedBalanceAfter;
	private Number projection;
	private Number budgetedProjection;
	private boolean isRealAccount;
	private Number id;

	public OverviewElement(String name, Number balanceBefore, Number balanceAfter, Number budgetedBalanceBefore,
			Number budgetedBalanceAfter, Number projection, Number budgetedProjection, Number id) {
		this.name = name;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
		this.budgetedBalanceBefore = budgetedBalanceBefore;
		this.budgetedBalanceAfter = budgetedBalanceAfter;
		this.projection = projection;
		this.budgetedProjection = budgetedProjection;
		this.isRealAccount = false;
		this.id = id;
	}

	public OverviewElement(VirtualAccount account, VirtaulAccountMonthData data) {
		this.name = account.getName();
		this.balanceBefore = data.getBalanceBefore();
		this.balanceAfter = data.getBalanceAfter();
		this.budgetedBalanceBefore = data.getBudgetedBalanceBefore();
		this.budgetedBalanceAfter = data.getBudgetedBalanceAfter();
		this.projection = data.getProjection();
		this.budgetedProjection = data.getBudgetedProjection();
		this.isRealAccount = false;
		this.id = account.getId();
	}

	public OverviewElement(String name, Number balanceBefore, Number balanceAfter, Number budgetedBalanceBefore,
			Number budgetedBalanceAfter, Number projection, Number budgetedProjection, boolean isRealAccount,
			Number id) {
		this.name = name;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
		this.budgetedBalanceBefore = budgetedBalanceBefore;
		this.budgetedBalanceAfter = budgetedBalanceAfter;
		this.projection = projection;
		this.budgetedProjection = budgetedProjection;
		this.isRealAccount = isRealAccount;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Number getBalanceBefore() {
		return balanceBefore;
	}

	public void setBalanceBefore(Number balanceBefore) {
		this.balanceBefore = balanceBefore;
	}

	public Number getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(Number balanceAfter) {
		this.balanceAfter = balanceAfter;
	}

	public Number getBudgetedBalanceBefore() {
		return budgetedBalanceBefore;
	}

	public void setBudgetedBalanceBefore(Number budgetedBalanceBefore) {
		this.budgetedBalanceBefore = budgetedBalanceBefore;
	}

	public Number getBudgetedBalanceAfter() {
		return budgetedBalanceAfter;
	}

	public void setBudgetedBalanceAfter(Number budgetedBalanceAfter) {
		this.budgetedBalanceAfter = budgetedBalanceAfter;
	}

	public Number getProjection() {
		return projection;
	}

	public void setProjection(Number projection) {
		this.projection = projection;
	}

	public boolean isRealAccount() {
		return isRealAccount;
	}

	public void setRealAccount(boolean isRealAccount) {
		this.isRealAccount = isRealAccount;
	}

	public Number getBudgetedProjection() {
		return budgetedProjection;
	}

	public void setBudgetedProjection(Number budgetedProjection) {
		this.budgetedProjection = budgetedProjection;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}
}