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

public class OverviewData {

	private final Number balanceAfter;

	private final Number budgetedBalanceAfter;

	private Number projection;

	private Number budgetedProjection;

	protected OverviewData(VirtualAccount virtualAccount, LocalDate startDate) {
		Predicate<Transaction> transactionInMonth = new TransactionInMonthPredicate(startDate);
		List<Transaction> credited = virtualAccount.getCreditedTransactions().stream().distinct()
				.filter(transactionInMonth).collect(Collectors.toList());
		List<Transaction> debited = virtualAccount.getDebitedTransactions().stream().distinct()
				.filter(transactionInMonth).collect(Collectors.toList());

		float balanceBefore = VirtaulAccountCalculator.getBalanceAt(virtualAccount, startDate,
				new EffectiveAmountFunction());

		float budgetedBalanceBefore = VirtaulAccountCalculator.getBalanceAt(virtualAccount, startDate,
				new BudgetedAmountFunction());

		this.balanceAfter = VirtaulAccountCalculator.getBalanceAfterTransactions(balanceBefore, credited, debited,
				new EffectiveAmountFunction());

		this.budgetedBalanceAfter = VirtaulAccountCalculator.getBalanceAfterTransactions(budgetedBalanceBefore,
				credited, debited, new BudgetedAmountFunction());

		if (virtualAccount.isPrebudgetedAccount()) {
			this.projection = budgetedBalanceAfter.floatValue() - balanceAfter.floatValue();
			this.budgetedProjection = null;
		} else {
			this.projection = VirtaulAccountCalculator.getBalanceAt(virtualAccount,
					startDate.withMonth(1).withDayOfMonth(1).plusYears(1), new EffectiveAmountFunction());

			this.budgetedProjection = VirtaulAccountCalculator.getBalanceAt(virtualAccount,
					startDate.withMonth(1).withDayOfMonth(1).plusYears(1), new BudgetedAmountFunction());
		}
	}

	public Number getBalanceAfter() {
		return balanceAfter;
	}

	public Number getBudgetedBalanceAfter() {
		return budgetedBalanceAfter;
	}

	public Number getProjection() {
		return projection;
	}

	public void setProjection(Number projection) {
		this.projection = projection;
	}

	public Number getBudgetedProjection() {
		return budgetedProjection;
	}

	public void setBudgetedProjection(Number budgetedProjection) {
		this.budgetedProjection = budgetedProjection;
	}

}
