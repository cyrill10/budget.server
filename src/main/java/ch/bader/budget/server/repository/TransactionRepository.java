package ch.bader.budget.server.repository;

import ch.bader.budget.server.adapter.sql.entity.TransactionDboSql;
import ch.bader.budget.server.adapter.sql.repository.TransactionJpaRepository;
import ch.bader.budget.server.boundary.time.MonthGenerator;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.mapper.TransactionMapper;
import ch.bader.budget.server.mapper.VirtualAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionRepository {

    @Autowired
    TransactionJpaRepository transactionRepository;

    @Autowired
    TransactionMapper transactionMapper;

    @Autowired
    VirtualAccountMapper virtualAccountMapper;


    public Transaction createTransaction(Transaction transaction) {
        TransactionDboSql transactionDboSql = transactionMapper.mapToOldEntity(
                transaction);
        transactionDboSql = transactionRepository.save(transactionDboSql);
        return transactionMapper.mapToDomain(transactionDboSql);
    }

    public Transaction updateTransaction(Transaction transaction) {
        TransactionDboSql transactionDboSql = transactionMapper.mapToOldEntity(
                transaction);
        transactionDboSql.updateEnums();
        transactionDboSql = transactionRepository.save(transactionDboSql);
        return transactionMapper.mapToDomain(transactionDboSql);
    }

    public void deleteTransaction(Integer transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    public void saveTransactions(List<Transaction> transactionList) {
        List<TransactionDboSql> newEntites = transactionList.stream()
                                                            .map(transactionMapper::mapToOldEntity)
                                                            .collect(
                                                                    Collectors.toList());
        transactionRepository.saveAll(newEntites);
    }

    public Transaction getTransactionById(Integer id) {
        TransactionDboSql transactionDboSql = transactionRepository.findById(id).orElseThrow();
        return transactionMapper.mapToDomain(transactionDboSql);
    }

    public List<Transaction> getAllTransactions(LocalDate date) {
        List<TransactionDboSql> transactionDboSqls = transactionRepository.findAllTransactionsInInterval(date,
                date.plusMonths(1));
        return transactionDboSqls.stream().map(transactionMapper::mapToDomain).sorted().collect(Collectors.toList());
    }

    public List<Transaction> getAllTransactionsUntilDate(LocalDate date) {
        List<TransactionDboSql> transactionDboSqls = transactionRepository.findAllTransactionsInInterval(MonthGenerator.STARTDATE,
                date);
        return transactionDboSqls.stream().map(transactionMapper::mapToDomain).sorted().collect(Collectors.toList());
    }

    public List<Transaction> getAllTransactionInInterval(LocalDate from, LocalDate to) {
        List<TransactionDboSql> transactionDboSqls = transactionRepository.findAllTransactionsInInterval(from,
                to);
        return transactionDboSqls.stream().map(transactionMapper::mapToDomain).sorted().collect(Collectors.toList());
    }

    public List<Transaction> getAllTransactionsForAccountUntilDate(int accountId, LocalDate date) {
        return transactionRepository
                .findAllTransactionsInIntervalForVirtualAccount(MonthGenerator.STARTDATE, date, accountId)
                .stream()
                .map(transactionMapper::mapToDomain)
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Transaction> getAllTransactionsForVirtualAccountsUntilDate(List<String> accountIds, LocalDate date) {
        List<Integer> ids = accountIds.stream().map(Integer::parseInt).collect(Collectors.toList());
        List<TransactionDboSql> transactionDboSqls = transactionRepository.findAllTransactionsInIntervalForVirtualAccounts(
                MonthGenerator.STARTDATE,
                date,
                ids);
        return transactionDboSqls.stream().map(transactionMapper::mapToDomain).sorted().collect(Collectors.toList());
    }
}
