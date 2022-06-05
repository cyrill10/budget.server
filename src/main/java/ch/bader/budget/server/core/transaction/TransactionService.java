package ch.bader.budget.server.core.transaction;

import ch.bader.budget.server.core.calculation.TransactionCalculator;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.TransactionElement;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.repository.TransactionRepository;
import ch.bader.budget.server.repository.VirtualAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    VirtualAccountRepository virtualAccountRepository;

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.createTransaction(transaction);
    }

    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.updateTransaction(transaction);
    }

    public void deleteTransaction(Integer transactionId) {
        transactionRepository.deleteTransaction(transactionId);
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

        transactionRepository.saveTransactions(newTransactions);
    }

    public Transaction getTransactionById(Integer id) {
        return transactionRepository.getTransactionById(id);
    }

    public List<Transaction> getAllTransactions(LocalDate date) {
        return transactionRepository.getAllTransactions(date);
    }

    public List<TransactionElement> getAllTransactionsForMonthAndVirtualAccount(LocalDate date, int accountId) {
        VirtualAccount virtualAccount = virtualAccountRepository.getAccountById(accountId);

        List<Transaction> allTransactionsForAccount = transactionRepository.getAllTransactionsForAccountUntilDate(
                accountId,
                date.plusMonths(1));

        return TransactionCalculator.getTransactionsForMonth(allTransactionsForAccount, virtualAccount, date);
    }

    public List<TransactionElement> getAllTransactionsForMonthAndRealAccount(LocalDate date, Integer accountId) {
        List<VirtualAccount> virtualAccounts = virtualAccountRepository.getAllVirtualAccountsForRealAccount(accountId);
        List<Transaction> allTransactionsForRealAccount = transactionRepository.getAllTransactionsForVirtualAccountsUntilDate(
                virtualAccounts.stream().map(VirtualAccount::getId).collect(
                        Collectors.toList()),
                date.plusMonths(1));

        return TransactionCalculator.getTransactionsForMonth(allTransactionsForRealAccount, virtualAccounts, date);
    }
}
