package ch.bader.budget.server.core.overview;

import ch.bader.budget.server.core.calculation.OverviewCalculator;
import ch.bader.budget.server.domain.OverviewElement;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.repository.RealAccountAdapter;
import ch.bader.budget.server.repository.TransactionAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class OverviewService {


    @Autowired
    @Qualifier("realAccountMongo")
    private RealAccountAdapter realAccountAdapter;

    @Autowired
    @Qualifier("transactionMongo")
    private TransactionAdapter transactionAdapter;

    public List<OverviewElement> getAllTransactions(LocalDate date) {
        LocalDate firstOfNextMonth = date.plusMonths(1);
        Map<RealAccount, List<VirtualAccount>> accountMap = realAccountAdapter.getAccountMap();
        List<Transaction> allTransactions = transactionAdapter.getAllTransactionsUntilDate(firstOfNextMonth);
        List<Transaction> allTransactionsTillEndOfYear = transactionAdapter.getAllTransactionInInterval(
            firstOfNextMonth,
            firstOfNextMonth.withMonth(1).withDayOfMonth(1).plusYears(1));

        return OverviewCalculator.getOverviewForMonth(accountMap,
            allTransactions,
            allTransactionsTillEndOfYear,
            firstOfNextMonth);
    }
}
