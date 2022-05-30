package ch.bader.budget.server.core.overview;

import ch.bader.budget.server.core.calculation.OverviewCalculator;
import ch.bader.budget.server.domain.OverviewElement;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.repository.RealAccountRepository;
import ch.bader.budget.server.repository.TransactionRepository;
import ch.bader.budget.server.repository.VirtualAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class OverviewService {


    @Autowired
    private RealAccountRepository realAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private VirtualAccountRepository virtualAccountRepository;

    public List<OverviewElement> getAllTransactions(LocalDate date) {
        LocalDate firstOfNextMonth = date.plusMonths(1);
        Map<RealAccount, List<VirtualAccount>> accountMap = realAccountRepository.getAccountMap();
        List<Transaction> allTransactions = transactionRepository.getAllTransactionsUntilDate(firstOfNextMonth);
        List<Transaction> allTransactionsTillEndOfYear = transactionRepository.getAllTransactionInInterval(
                firstOfNextMonth,
                firstOfNextMonth.withMonth(1).withDayOfMonth(1).plusYears(1));

        return OverviewCalculator.getOverviewForMonth(accountMap,
                allTransactions,
                allTransactionsTillEndOfYear,
                firstOfNextMonth);
    }
}
