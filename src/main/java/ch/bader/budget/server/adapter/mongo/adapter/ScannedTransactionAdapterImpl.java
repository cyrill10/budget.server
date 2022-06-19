package ch.bader.budget.server.adapter.mongo.adapter;

import ch.bader.budget.server.adapter.mongo.entity.ScannedTransactionDbo;
import ch.bader.budget.server.adapter.mongo.repository.ScannedTransactionMongoRepository;
import ch.bader.budget.server.domain.ScannedTransaction;
import ch.bader.budget.server.mapper.ScannedTransactionMapper;
import ch.bader.budget.server.repository.ScannedTransactionAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component("scannedTransactionMongo")
public class ScannedTransactionAdapterImpl implements ScannedTransactionAdapter {

    @Autowired
    ScannedTransactionMapper scannedTransactionMapper;

    @Autowired
    ScannedTransactionMongoRepository scannedTransactionMongoRepository;

    @Override
    public List<ScannedTransaction> saveAll(List<ScannedTransaction> transactionList) {
        List<ScannedTransactionDbo> entities = transactionList.stream()
                                                              .map(scannedTransactionMapper::mapToEntity)
                                                              .collect(
                                                                  Collectors.toList());
        entities = scannedTransactionMongoRepository.saveAll(entities);
        return entities.stream().map(scannedTransactionMapper::mapToDomain).collect(Collectors.toList());
    }

    @Override
    public List<ScannedTransaction> getTransactionsForYearMonth(YearMonth yearMonth) {
        return scannedTransactionMongoRepository
            .findAllByYearMonth(yearMonth)
            .stream()
            .map(scannedTransactionMapper::mapToDomain)
            .sorted()
            .collect(Collectors.toList());
    }

    @Override
    public List<ScannedTransaction> findAllById(List<String> transactionIds) {
        return StreamSupport
            .stream(scannedTransactionMongoRepository.findAllById(transactionIds).spliterator(), false)
            .map(scannedTransactionMapper::mapToDomain)
            .sorted()
            .collect(Collectors.toList());
    }
}
