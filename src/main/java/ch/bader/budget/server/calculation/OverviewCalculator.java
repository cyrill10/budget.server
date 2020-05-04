package ch.bader.budget.server.calculation;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ch.bader.budget.server.entity.RealAccount;
import ch.bader.budget.server.entity.VirtualAccount;
import ch.bader.budget.server.json.OverviewElement;

public class OverviewCalculator {

	public static Iterable<OverviewElement> getOverviewForMonth(List<RealAccount> accounts, LocalDate withDayOfMonth) {
		return accounts.stream().distinct().map(accout -> getOverviewElementListFromRealAccount(accout, withDayOfMonth))
				.flatMap(l -> l.stream()).collect(Collectors.toList());
	}

	public static List<OverviewElement> getOverviewElementListFromRealAccount(RealAccount account,
			LocalDate withDayOfMonth) {
		LinkedList<OverviewElement> elements = account.getVirtualAccounts().stream().distinct()
				.map(virtualAccount -> getOverviewElementFromVirtualAccount(virtualAccount, withDayOfMonth))
				.collect(Collectors.toCollection(LinkedList::new));
		elements.push(createOverviewElement(account, elements));
		return elements;

	}

	public static OverviewElement getOverviewElementFromVirtualAccount(VirtualAccount account,
			LocalDate withDayOfMonth) {
		VirtaulAccountMonthData data = new VirtaulAccountMonthData(account, withDayOfMonth);
		return new OverviewElement(account, data);
	}

	private static OverviewElement createOverviewElement(RealAccount account, List<OverviewElement> elements) {
		Balance balance = new Balance(0);
		elements.stream().distinct().map(e -> e.getBalanceBefore()).forEach(balance::add);
		float balanceBefore = balance.getBalance();

		balance = new Balance(0);
		elements.stream().distinct().map(e -> e.getBalanceAfter()).forEach(balance::add);
		float balanceAfter = balance.getBalance();

		balance = new Balance(0);
		elements.stream().distinct().map(e -> e.getBudgetedBalanceBefore()).forEach(balance::add);
		float budgetedBalanceBefore = balance.getBalance();

		balance = new Balance(0);
		elements.stream().distinct().map(e -> e.getBudgetedBalanceAfter()).forEach(balance::add);
		float budgetedBalanceAfter = balance.getBalance();

		balance = new Balance(0);
		elements.stream().distinct().map(e -> e.getProjection()).forEach(balance::add);
		float projection = balance.getBalance();

		balance = new Balance(0);
		elements.stream().distinct().map(e -> e.getBudgetedProjection()).forEach(balance::add);
		float budgetedProjection = balance.getBalance();

		return new OverviewElement(account.getName(), balanceBefore, balanceAfter, budgetedBalanceBefore,
				budgetedBalanceAfter, projection, budgetedProjection, true, account.getId());
	}

}
