package ch.bader.budget.server.json;

import ch.bader.budget.server.calculation.OverviewData;
import ch.bader.budget.server.entity.VirtualAccount;

public class OverviewElement {

	private String name;
	private Number balanceAfter;
	private Number budgetedBalanceAfter;
	private Number projection;
	private Number budgetedProjection;
	private boolean isRealAccount;
	private Number id;

	public OverviewElement(String name, Number balanceAfter, Number budgetedBalanceAfter, Number projection,
			Number budgetedProjection, Number id) {
		this.name = name;
		this.balanceAfter = balanceAfter;
		this.budgetedBalanceAfter = budgetedBalanceAfter;
		this.projection = projection;
		this.budgetedProjection = budgetedProjection;
		this.isRealAccount = false;
		this.id = id;
	}

	public OverviewElement(VirtualAccount account, OverviewData data) {
		this.name = account.getName();
		this.balanceAfter = data.getBalanceAfter();
		this.budgetedBalanceAfter = data.getBudgetedBalanceAfter();
		this.projection = data.getProjection();
		this.budgetedProjection = data.getBudgetedProjection();
		this.isRealAccount = false;
		this.id = account.getId();
	}

	public OverviewElement(String name, Number balanceAfter, Number budgetedBalanceAfter, Number projection,
			Number budgetedProjection, boolean isRealAccount, Number id) {
		this.name = name;
		this.balanceAfter = balanceAfter;
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

	public Number getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(Number balanceAfter) {
		this.balanceAfter = balanceAfter;
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