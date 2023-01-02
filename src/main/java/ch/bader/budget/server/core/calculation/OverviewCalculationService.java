package ch.bader.budget.server.core.calculation;

import ch.bader.budget.server.core.calculation.predicate.OverviewRealAccountPredicate;
import ch.bader.budget.server.domain.OverviewElement;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.VirtualAccount;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OverviewCalculationService {

    private final BalanceService balanceService;

    public OverviewCalculationService(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    public List<OverviewElement> calculateOverview(
            Map<RealAccount, List<VirtualAccount>> accountMap,
            List<Transaction> allTransactionsTillEndOfYear, LocalDate firstOfMonth) {
        LocalDate firstOfNextMonth = firstOfMonth.plusMonths(1);

        List<Transaction> transactionsTillDate = allTransactionsTillEndOfYear.stream()
                .filter(transaction -> firstOfNextMonth.isAfter(transaction.getDate()))
                .collect(Collectors.toList());

        return accountMap.entrySet()
                .stream()
                .filter(entry -> new OverviewRealAccountPredicate().test(entry.getKey()))
                .map(account -> getOverviewElementListFromRealAccount(account.getKey(),
                        account.getValue(), transactionsTillDate,
                        allTransactionsTillEndOfYear, firstOfMonth, firstOfNextMonth))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

    }

    private List<OverviewElement> getOverviewElementListFromRealAccount(
            RealAccount realAccount, List<VirtualAccount> virtualAccounts,
            List<Transaction> allTransactionsTillDate,
            List<Transaction> allTransactionsTillEndOfYear, LocalDate firstOfMonth,
            LocalDate firstOfNextMonth) {


        Balance realAccountBalanceEndOfMonth = new Balance();
        Balance realAccountBalanceEndOfYear = realAccount.isPrebudgetedAccount() ? new Balance(
                BigDecimal.ZERO, null) : new Balance();

        LinkedList<OverviewElement> overviewElements = new LinkedList<>();

        virtualAccounts.forEach(virtualAccount -> {
                    if (virtualAccount.isPrebudgetedAccount()) {
                        addBalancePrebudgetedAccount(virtualAccount, allTransactionsTillDate,
                                firstOfMonth, realAccountBalanceEndOfMonth,
                                realAccountBalanceEndOfYear, overviewElements);
                    } else {
                        addBalanceNormalAccount(virtualAccount, allTransactionsTillDate,
                                allTransactionsTillEndOfYear,
                                firstOfNextMonth, realAccountBalanceEndOfMonth,
                                realAccountBalanceEndOfYear,
                                overviewElements);
                    }
                }
        );
        OverviewElement realAccountElement = new OverviewElement(realAccount.getName(),
                realAccountBalanceEndOfMonth.getEffective(),
                realAccountBalanceEndOfMonth.getBudgeted(),
                realAccountBalanceEndOfYear.getEffective(),
                realAccountBalanceEndOfYear.getBudgeted(),
                true,
                realAccount.getId());
        overviewElements.push(realAccountElement);

        return overviewElements;
    }

    private void addBalanceNormalAccount(VirtualAccount virtualAccount,
                                         List<Transaction> allTransactionsTillDate,
                                         List<Transaction> allTransactionsTillEndOfYear,
                                         LocalDate firstOfNextMonth,
                                         Balance realAccountBalanceEndOfMonth,
                                         Balance realAccountBalanceEndOfYear,
                                         LinkedList<OverviewElement> overviewElements) {
        Balance balanceEndOfTheMonth =
                balanceService.calculateBalanceAt(allTransactionsTillDate,
                        List.of(virtualAccount), virtualAccount.getUnderlyingAccount(),
                        firstOfNextMonth);

        Balance balanceEndOfYear =
                balanceService.calculateBalanceWithinRange(balanceEndOfTheMonth,
                        allTransactionsTillEndOfYear,
                        List.of(virtualAccount), virtualAccount.getUnderlyingAccount(),
                        firstOfNextMonth,
                        firstOfNextMonth.withMonth(1).withDayOfMonth(1).plusYears(1));

        realAccountBalanceEndOfYear.add(balanceEndOfYear);
        realAccountBalanceEndOfMonth.add(balanceEndOfTheMonth);

        overviewElements.add(new OverviewElement(virtualAccount.getName(),
                balanceEndOfTheMonth.getEffective(),
                balanceEndOfTheMonth.getBudgeted(),
                balanceEndOfYear.getEffective(),
                balanceEndOfYear.getBudgeted(),
                false,
                virtualAccount.getId()));
    }

    private void addBalancePrebudgetedAccount(VirtualAccount virtualAccount,
                                              List<Transaction> allTransactionsTillDate,
                                              LocalDate firstOfMonth,
                                              Balance realAccountBalanceEndOfMonth,
                                              Balance realAccountBalanceEndOfYear,
                                              LinkedList<OverviewElement> overviewElements) {
        Balance balanceBeginningOfMonth =
                new Balance();

        Balance balanceEndOfTheMonth =
                balanceService.calculateBalanceWithinRange(balanceBeginningOfMonth,
                        allTransactionsTillDate, List.of(virtualAccount),
                        virtualAccount.getUnderlyingAccount(),
                        firstOfMonth, firstOfMonth.plusMonths(1));

        Balance differenceBudgetedAndEffective = new Balance(balanceEndOfTheMonth.getBudgeted()
                .subtract(balanceEndOfTheMonth.getEffective()), null);

        realAccountBalanceEndOfYear.add(differenceBudgetedAndEffective);
        realAccountBalanceEndOfMonth.add(balanceEndOfTheMonth);

        overviewElements.add(new OverviewElement(virtualAccount.getName(),
                balanceEndOfTheMonth.getEffective(),
                balanceEndOfTheMonth.getBudgeted(),
                differenceBudgetedAndEffective.getEffective(),
                differenceBudgetedAndEffective.getBudgeted(),
                false,
                virtualAccount.getId()));
    }
}

