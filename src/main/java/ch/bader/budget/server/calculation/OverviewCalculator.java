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
		return accounts.parallelStream().map(accout -> getOverviewElementListFromRealAccount(accout, withDayOfMonth))
				.flatMap(l -> l.stream()).collect(Collectors.toList());
	}

	public static List<OverviewElement> getOverviewElementListFromRealAccount(RealAccount account,
			LocalDate withDayOfMonth) {
		LinkedList<OverviewElement> elements = account.getVirtualAccounts().parallelStream()
				.map(virtualAccount -> getOverviewElementFromVirtualAccount(virtualAccount, withDayOfMonth))
				.collect(Collectors.toCollection(LinkedList::new));
		elements.push(new OverviewElement(account, elements));
		return elements;

	}

	public static OverviewElement getOverviewElementFromVirtualAccount(VirtualAccount account,
			LocalDate withDayOfMonth) {
		VirtaulAccountMonthData data = new VirtaulAccountMonthData(account, withDayOfMonth);
		return new OverviewElement(account, data);
	}

}
