package ch.bader.budget.server.core.overview;

import ch.bader.budget.server.core.calculation.OverviewCalculationService;
import ch.bader.budget.server.domain.OverviewElement;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.repository.RealAccountAdapter;
import ch.bader.budget.server.repository.TransactionAdapter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class OverviewService {

    private final RealAccountAdapter realAccountAdapter;

    private final TransactionAdapter transactionAdapter;

    private final OverviewCalculationService overviewCalculationService;

    public OverviewService(RealAccountAdapter realAccountAdapter,
                           TransactionAdapter transactionAdapter,
                           OverviewCalculationService overviewCalculationService) {
        this.realAccountAdapter = realAccountAdapter;
        this.transactionAdapter = transactionAdapter;
        this.overviewCalculationService = overviewCalculationService;
    }

    public List<OverviewElement> getAllTransactions(LocalDate date) {
        LocalDate firstOfNextMonth = date.plusMonths(1);
        Map<RealAccount, List<VirtualAccount>> accountMap = realAccountAdapter.getAccountMap();
        List<Transaction> allTransactionsTilEndOfYear =
                transactionAdapter.getAllTransactionsUntilDate(
                        firstOfNextMonth.withMonth(1).withDayOfMonth(1).plusYears(1));

        return overviewCalculationService.calculateOverview(accountMap,
                allTransactionsTilEndOfYear, date);
    }
}
