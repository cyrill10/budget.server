package ch.bader.budget.server.calculation;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ch.bader.budget.server.entity.RealAccount;
import ch.bader.budget.server.entity.VirtualAccount;
import ch.bader.budget.server.json.OverviewElement;

public class OverviewCalculator {

	public static Iterable<OverviewElement> getOverviewForMonth(List<RealAccount> accounts, LocalDate withDayOfMonth) {
		return accounts.stream().distinct().map(accout -> getOverviewElementListFromRealAccount(accout, withDayOfMonth))
				.flatMap(Collection::stream).collect(Collectors.toList());
	}

	public static List<OverviewElement> getOverviewElementListFromRealAccount(RealAccount account,
			LocalDate withDayOfMonth) {
		LinkedList<OverviewElement> elements = account.getVirtualAccounts().stream().distinct()
				.filter(va -> !va.isDeleted())
				.map(virtualAccount -> getOverviewElementFromVirtualAccount(virtualAccount, withDayOfMonth))
				.collect(Collectors.toCollection(LinkedList::new));
		elements.push(createOverviewElement(account, elements));
		return elements;

	}

	public static OverviewElement getOverviewElementFromVirtualAccount(VirtualAccount account,
			LocalDate withDayOfMonth) {
		OverviewData data = new OverviewData(account, withDayOfMonth);
		return new OverviewElement(account, data);
	}

	private static OverviewElement createOverviewElement(RealAccount account, List<OverviewElement> elements) {
		Balance balance = new Balance(0);
		elements.stream().distinct().map(OverviewElement::getBalanceAfter).forEach(balance::add);
		float balanceAfter = balance.getBalance();

		balance = new Balance(0);
		elements.stream().distinct().map(OverviewElement::getBudgetedBalanceAfter).forEach(balance::add);
		float budgetedBalanceAfter = balance.getBalance();

		Number projection;
		Number budgetedProjection;

		if (account.isPrebudgetedAccount()) {
			projection = budgetedBalanceAfter - balanceAfter;
			budgetedProjection = null;

		} else {
			balance = new Balance(0);
			elements.stream().distinct().map(OverviewElement::getProjection).forEach(balance::add);
			projection = balance.getBalance();

			balance = new Balance(0);
			elements.stream().distinct().map(OverviewElement::getBudgetedProjection).forEach(balance::add);
			budgetedProjection = balance.getBalance();
		}

		return new OverviewElement(account.getName(), balanceAfter, budgetedBalanceAfter, projection,
				budgetedProjection, true, account.getId());
	}

}
