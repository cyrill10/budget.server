package ch.bader.budget.server.calculation;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ch.bader.budget.server.calculation.implementation.function.BudgetedAmountFunction;
import ch.bader.budget.server.calculation.implementation.function.EffectiveAmountFunction;
import ch.bader.budget.server.calculation.implementation.predicate.TransactionInMonthPredicate;
import ch.bader.budget.server.entity.Transaction;
import ch.bader.budget.server.entity.VirtualAccount;

public class VirtaulAccountMonthData {

	private float balanceBefore;

	private float budgetedBalanceBefore;

	private float balanceAfter;

	private float budgetedBalanceAfter;

	private float projection;

	private float budgetedProjection;

	protected VirtaulAccountMonthData(VirtualAccount virtualAccount, LocalDate startDate) {
		Predicate<Transaction> transactionInMonth = new TransactionInMonthPredicate(startDate);
		List<Transaction> credited = virtualAccount.getCreditedTransactions().stream().distinct()
				.filter(transactionInMonth).collect(Collectors.toList());
		List<Transaction> debited = virtualAccount.getDebitedTransactions().stream().distinct()
				.filter(transactionInMonth).collect(Collectors.toList());

		this.balanceBefore = VirtaulAccountCalculator.getBalanceAt(virtualAccount, startDate,
				new EffectiveAmountFunction());

		this.budgetedBalanceBefore = VirtaulAccountCalculator.getBalanceAt(virtualAccount, startDate,
				new BudgetedAmountFunction());

		this.balanceAfter = VirtaulAccountCalculator.getBalanceAfterTransactions(balanceBefore, credited, debited,
				new EffectiveAmountFunction());

		this.budgetedBalanceAfter = VirtaulAccountCalculator.getBalanceAfterTransactions(budgetedBalanceBefore,
				credited, debited, new BudgetedAmountFunction());

		this.projection = VirtaulAccountCalculator.getBalanceAt(virtualAccount,
				startDate.withMonth(1).withDayOfMonth(1).plusYears(1), new EffectiveAmountFunction());

		this.budgetedProjection = VirtaulAccountCalculator.getBalanceAt(virtualAccount,
				startDate.withMonth(1).withDayOfMonth(1).plusYears(1), new BudgetedAmountFunction());

	}

	public float getBudgetedBalanceBefore() {
		return budgetedBalanceBefore;
	}

	public float getBalanceBefore() {
		return balanceBefore;
	}

	public float getBalanceAfter() {
		return balanceAfter;
	}

	public float getBudgetedBalanceAfter() {
		return budgetedBalanceAfter;
	}

	public float getProjection() {
		return projection;
	}

	public void setProjection(float projection) {
		this.projection = projection;
	}

	public float getBudgetedProjection() {
		return budgetedProjection;
	}

	public void setBudgetedProjection(float budgetedProjection) {
		this.budgetedProjection = budgetedProjection;
	}

}
