package ch.bader.budget.server.core.calculation;

import ch.bader.budget.server.core.calculation.implementation.predicate.OverviewRealAccountPredicate;
import ch.bader.budget.server.domain.OverviewElement;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.VirtualAccount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OverviewCalculator {

    public static List<OverviewElement> getOverviewForMonth(Map<RealAccount, List<VirtualAccount>> accountMap,
                                                            List<Transaction> allTransactions,
                                                            List<Transaction> allTransactionsTillEndOfYear,
                                                            LocalDate utilDate) {
        return accountMap.entrySet()
                         .stream()
                         .distinct()
                         .filter(entry -> new OverviewRealAccountPredicate().test(entry.getKey()))
                         .map(account -> getOverviewElementListFromRealAccount(account,
                             allTransactions, allTransactionsTillEndOfYear, utilDate))
                         .flatMap(Collection::stream)
                         .collect(Collectors.toList());
    }

    public static List<OverviewElement> getOverviewElementListFromRealAccount(
        Map.Entry<RealAccount, List<VirtualAccount>> accountEntry, List<Transaction> allTransactions,
        List<Transaction> allTransactionsTillEndOfYear, LocalDate untilDate) {
        LinkedList<OverviewElement> elements = accountEntry.getValue().stream().distinct()
                                                           .filter(va -> !va.isDeleted())
                                                           .map(virtualAccount -> getOverviewElementFromVirtualAccount(
                                                               virtualAccount,
                                                               allTransactions.stream()
                                                                              .filter(t -> t.getCreditedAccount()
                                                                                            .equals(virtualAccount) || t
                                                                                  .getDebitedAccount()
                                                                                  .equals(virtualAccount))
                                                                              .collect(Collectors.toList()),
                                                               allTransactionsTillEndOfYear.stream()
                                                                                           .filter(t -> t
                                                                                               .getCreditedAccount()
                                                                                               .equals(virtualAccount) || t
                                                                                               .getDebitedAccount()
                                                                                               .equals(virtualAccount))
                                                                                           .collect(Collectors.toList()),
                                                               untilDate))
                                                           .collect(Collectors.toCollection(LinkedList::new));
        elements.push(createOverviewElement(accountEntry.getKey(), elements));
        return elements;

    }

    public static OverviewElement getOverviewElementFromVirtualAccount(VirtualAccount account,
                                                                       List<Transaction> transactions,
                                                                       List<Transaction> transactionsTillEndOfYear,
                                                                       LocalDate untilDate) {
        OverviewData data = new OverviewData(account, transactions, transactionsTillEndOfYear, untilDate);
        return new OverviewElement(account, data);
    }

    private static OverviewElement createOverviewElement(RealAccount account, List<OverviewElement> elements) {
        Balance realAccountBalance = new Balance(BigDecimal.ZERO, BigDecimal.ZERO);
        Balance projectionBalance = new Balance(BigDecimal.ZERO, BigDecimal.ZERO);
        elements.stream()
                .distinct()
                .forEach(element -> {
                    realAccountBalance.add(element.getBalanceAfter(),
                        element.getBudgetedBalanceAfter());
                    projectionBalance.add(element.getProjection(),
                        element.getBudgetedProjection() != null ? element.getBudgetedProjection() : BigDecimal.ZERO);
                });


        BigDecimal projection;
        BigDecimal budgetedProjection;

        if (account.isPrebudgetedAccount()) {
            projection = realAccountBalance.getBudgeted().subtract(realAccountBalance.getEffective());
            budgetedProjection = null;

        } else {
            projection = projectionBalance.getEffective();
            budgetedProjection = projectionBalance.getBudgeted();
        }

        return new OverviewElement(account.getName(),
            realAccountBalance.getEffective(),
            realAccountBalance.getBudgeted(),
            projection,
            budgetedProjection,
            true,
            account.getId());
    }

}
