package ch.bader.budget.server.core.transaction;

import ch.bader.budget.server.core.calculation.TransactionCalculator;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.TransactionElement;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.repository.TransactionAdapter;
import ch.bader.budget.server.repository.VirtualAccountAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    @Qualifier("transactionMongo")
    TransactionAdapter transactionAdapter;

    @Autowired
    @Qualifier("virtualAccountMongo")
    private VirtualAccountAdapter virtualAccountAdapter;

    public Transaction createTransaction(Transaction transaction) {
        return transactionAdapter.createTransaction(transaction);
    }

    public Transaction updateTransaction(Transaction transaction) {
        return transactionAdapter.updateTransaction(transaction);
    }

    public void deleteTransaction(String transactionId) {
        transactionAdapter.deleteTransaction(transactionId);
    }

    public void dublicateTransaction(Transaction transaction) {
        LocalDate startDate = transaction.getDate();
        LocalDate endDate = transaction.getDate()
                                       .plusYears(1)
                                       .withDayOfMonth(1)
                                       .withMonth(1);
        List<Transaction> newTransactions = new ArrayList<>();

        while (startDate.isBefore(endDate)) {
            startDate = startDate.plusMonths(1);
            newTransactions.add(transaction.createDuplicate(startDate));
        }

        transactionAdapter.saveTransactions(newTransactions);
    }

    public Transaction getTransactionById(String id) {
        return transactionAdapter.getTransactionById(id);
    }

    public List<Transaction> getAllTransactions(LocalDate date) {
        return transactionAdapter.getAllTransactions(date).stream().sorted().collect(Collectors.toList());
    }

    public List<TransactionElement> getAllTransactionsForMonthAndVirtualAccount(LocalDate date, String accountId) {
        VirtualAccount virtualAccount = virtualAccountAdapter.getAccountById(accountId);

        List<Transaction> allTransactionsForAccount = transactionAdapter.getAllTransactionsForVirtualAccountUntilDate(
            accountId,
            date.plusMonths(1));

        return TransactionCalculator.getTransactionsForMonth(allTransactionsForAccount, virtualAccount, date);
    }

    public List<TransactionElement> getAllTransactionsForMonthAndRealAccount(LocalDate date, String accountId) {
        List<VirtualAccount> virtualAccounts = virtualAccountAdapter.getAllVirtualAccountsForRealAccount(
            accountId);
        List<Transaction> allTransactionsForRealAccount = transactionAdapter.getAllTransactionsForVirtualAccountsUntilDate(
            virtualAccounts.stream().map(VirtualAccount::getId).collect(
                Collectors.toList()),
            date.plusMonths(1));

        return TransactionCalculator.getTransactionsForMonth(allTransactionsForRealAccount, virtualAccounts, date);
    }
}
