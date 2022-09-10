package ch.bader.budget.server.adapter.mongo.adapter;

import ch.bader.budget.server.adapter.mongo.entity.TransactionDbo;
import ch.bader.budget.server.adapter.mongo.repository.TransactionMongoRepository;
import ch.bader.budget.server.boundary.time.MonthGenerator;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.mapper.TransactionMapper;
import ch.bader.budget.server.repository.TransactionAdapter;
import ch.bader.budget.server.repository.VirtualAccountAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service("transactionMongo")
public class TransactionAdapterImpl implements TransactionAdapter {

    private final TransactionMapper transactionMapper;

    private final TransactionMongoRepository transactionMongoRepository;

    private final VirtualAccountAdapter virtualAccountAdapter;

    public TransactionAdapterImpl(TransactionMapper transactionMapper,
                                  TransactionMongoRepository transactionMongoRepository,
                                  @Qualifier("virtualAccountMongo") VirtualAccountAdapter virtualAccountAdapter) {
        this.transactionMapper = transactionMapper;
        this.transactionMongoRepository = transactionMongoRepository;
        this.virtualAccountAdapter = virtualAccountAdapter;
    }


    @Override
    public Transaction updateTransaction(Transaction transaction) {
        TransactionDbo transactionDbo = transactionMapper.mapToEntity(
            transaction);
        transactionDbo = transactionMongoRepository.save(transactionDbo);
        Transaction transactionSaved = transactionMapper.mapToDomain(transactionDbo);
        transactionSaved.setCreditedAccount(transaction.getCreditedAccount());
        transactionSaved.setDebitedAccount(transaction.getDebitedAccount());
        return transactionSaved;
    }

    @Override
    public void deleteTransaction(String transactionId) {
        transactionMongoRepository.deleteById(transactionId);
    }

    @Override
    public void saveTransactions(List<Transaction> transactionList) {
        List<TransactionDbo> newEntites = transactionList.stream()
                                                         .map(transactionMapper::mapToEntity)
                                                         .collect(
                                                             Collectors.toList());
        transactionMongoRepository.saveAll(newEntites);
    }

    @Override
    public Transaction getTransactionById(String id) {
        TransactionDbo transactionDbo = transactionMongoRepository.findById(id).orElseThrow();
        Transaction transaction = transactionMapper.mapToDomain(transactionDbo);
        VirtualAccount debitedAccount = virtualAccountAdapter.getAccountById(transactionDbo.getDebitedAccountId());
        VirtualAccount creditedAccount = virtualAccountAdapter.getAccountById(transactionDbo.getCreditedAccountId());
        transaction.setDebitedAccount(debitedAccount);
        transaction.setCreditedAccount(creditedAccount);
        return transaction;
    }

    @Override
    public List<Transaction> getAllTransactions(LocalDate date) {
        List<TransactionDbo> transactionDbos = transactionMongoRepository.findAllByDateBetween(date.minusDays(1),
            date.plusMonths(1));

        return addAccountsAndTransform(transactionDbos);
    }

    @Override
    public List<Transaction> getAllTransactionsUntilDate(LocalDate unitlExclusive) {
        List<TransactionDbo> transactionDbos = transactionMongoRepository
            .findAllByDateBetween(MonthGenerator.STARTDATE.minusDays(1), unitlExclusive);

        return addAccountsAndTransform(transactionDbos);

    }

    @Override
    public List<Transaction> getAllTransactionInInterval(LocalDate fromInclusive, LocalDate toExclusive) {
        List<TransactionDbo> transactionDbos = transactionMongoRepository
            .findAllByDateBetween(fromInclusive.minusDays(1), toExclusive);

        return addAccountsAndTransform(transactionDbos);

    }

    @Override
    public List<Transaction> getAllTransactionsForVirtualAccountUntilDate(String virtualAccountId,
                                                                          LocalDate unitlExclusive) {
        List<TransactionDbo> transactionDbos = transactionMongoRepository.findAllByDateBetweenAndVirtualAccountId(
            MonthGenerator.STARTDATE.minusDays(1),
            unitlExclusive,
            List.of(virtualAccountId));
        return addAccountsAndTransform(transactionDbos);
    }

    @Override
    public List<Transaction> getAllTransactionsForVirtualAccountsUntilDate(List<String> accountIds,
                                                                           LocalDate unitlExclusive) {
        List<TransactionDbo> transactionDbos = transactionMongoRepository.findAllByDateBetweenAndVirtualAccountId(
            MonthGenerator.STARTDATE.minusDays(1),
            unitlExclusive,
            accountIds);
        return addAccountsAndTransform(transactionDbos);
    }

    private Transaction mapToDomainAndAddVirtualAccounts(TransactionDbo transactionDbo,
                                                         List<VirtualAccount> virtualAccounts) {
        Transaction transaction = transactionMapper.mapToDomain(transactionDbo);
        VirtualAccount debitedAccount = virtualAccounts
            .stream()
            .filter(va -> va.getId().equals(transactionDbo.getDebitedAccountId()))
            .findAny()
            .orElseThrow();
        VirtualAccount creditedAccount = virtualAccounts
            .stream()
            .filter(va -> va.getId().equals(transactionDbo.getCreditedAccountId()))
            .findAny()
            .orElseThrow();
        transaction.setDebitedAccount(debitedAccount);
        transaction.setCreditedAccount(creditedAccount);
        return transaction;
    }

    private List<Transaction> addAccountsAndTransform(List<TransactionDbo> transactionDbos) {
        List<VirtualAccount> virtualAccounts = virtualAccountAdapter.getAllVirtualAccountsWithTheirUnderlyingAccount();

        return transactionDbos
            .stream()
            .map(t -> mapToDomainAndAddVirtualAccounts(t, virtualAccounts))
            .sorted()
            .collect(Collectors.toList());
    }

}
