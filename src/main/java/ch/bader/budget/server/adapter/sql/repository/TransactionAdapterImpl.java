package ch.bader.budget.server.adapter.sql.repository;

import ch.bader.budget.server.adapter.sql.entity.TransactionDboSql;
import ch.bader.budget.server.adapter.sql.repository.jpa.TransactionJpaRepository;
import ch.bader.budget.server.boundary.time.MonthGenerator;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.mapper.TransactionMapper;
import ch.bader.budget.server.mapper.VirtualAccountMapper;
import ch.bader.budget.server.repository.TransactionAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component("transactionMySql")
public class TransactionAdapterImpl implements TransactionAdapter {

    @Autowired
    TransactionJpaRepository transactionRepository;

    @Autowired
    TransactionMapper transactionMapper;

    @Autowired
    VirtualAccountMapper virtualAccountMapper;


    @Override
    public Transaction createTransaction(Transaction transaction) {
        TransactionDboSql transactionDboSql = transactionMapper.mapToOldEntity(
            transaction);
        transactionDboSql = transactionRepository.save(transactionDboSql);
        return transactionMapper.mapToDomain(transactionDboSql);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        TransactionDboSql transactionDboSql = transactionMapper.mapToOldEntity(
            transaction);
        transactionDboSql.updateEnums();
        transactionDboSql = transactionRepository.save(transactionDboSql);
        transactionDboSql.reloadEnums();
        return transactionMapper.mapToDomain(transactionDboSql);
    }

    @Override
    public void deleteTransaction(String transactionId) {
        transactionRepository.deleteById(Integer.parseInt(transactionId));
    }

    @Override
    public void saveTransactions(List<Transaction> transactionList) {
        List<TransactionDboSql> newEntites = transactionList.stream()
                                                            .map(transactionMapper::mapToOldEntity)
                                                            .collect(
                                                                Collectors.toList());
        transactionRepository.saveAll(newEntites);
    }

    @Override
    public Transaction getTransactionById(String id) {
        TransactionDboSql transactionDboSql = transactionRepository.findById(Integer.parseInt(id)).orElseThrow();
        return transactionMapper.mapToDomain(transactionDboSql);
    }

    @Override
    public List<Transaction> getAllTransactions(LocalDate date) {
        List<TransactionDboSql> transactionDboSqls = transactionRepository.findAllTransactionsInInterval(date,
            date.plusMonths(1));
        return transactionDboSqls.stream().map(transactionMapper::mapToDomain).sorted().collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getAllTransactionsUntilDate(LocalDate unitlExclusive) {
        List<TransactionDboSql> transactionDboSqls = transactionRepository.findAllTransactionsInInterval(MonthGenerator.STARTDATE,
            unitlExclusive);
        return transactionDboSqls.stream().map(transactionMapper::mapToDomain).sorted().collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getAllTransactionInInterval(LocalDate fromInclusive, LocalDate toExclusive) {
        List<TransactionDboSql> transactionDboSqls = transactionRepository.findAllTransactionsInInterval(fromInclusive,
            toExclusive);
        return transactionDboSqls.stream().map(transactionMapper::mapToDomain).sorted().collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getAllTransactionsForVirtualAccountUntilDate(String accountId, LocalDate unitlExclusive) {
        return transactionRepository
            .findAllTransactionsInIntervalForVirtualAccount(MonthGenerator.STARTDATE,
                unitlExclusive,
                Integer.parseInt(accountId))
            .stream()
            .map(transactionMapper::mapToDomain)
            .sorted()
            .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getAllTransactionsForVirtualAccountsUntilDate(List<String> accountIds,
                                                                           LocalDate unitlExclusive) {
        List<Integer> ids = accountIds.stream().map(Integer::parseInt).collect(Collectors.toList());
        List<TransactionDboSql> transactionDboSqls = transactionRepository.findAllTransactionsInIntervalForVirtualAccounts(
            MonthGenerator.STARTDATE,
            unitlExclusive,
            ids);
        return transactionDboSqls.stream().map(transactionMapper::mapToDomain).sorted().collect(Collectors.toList());
    }
}
