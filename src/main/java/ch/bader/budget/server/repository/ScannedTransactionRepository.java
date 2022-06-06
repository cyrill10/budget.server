package ch.bader.budget.server.repository;

import ch.bader.budget.server.adapter.sql.entity.ClosingProcessDboSql;
import ch.bader.budget.server.adapter.sql.entity.ScannedTransactionDboSql;
import ch.bader.budget.server.adapter.sql.repository.jpa.ClosingProcessJpaRepository;
import ch.bader.budget.server.adapter.sql.repository.jpa.ScannedTransactionJpaRepository;
import ch.bader.budget.server.domain.ScannedTransaction;
import ch.bader.budget.server.mapper.ScannedTransactionMapper;
import ch.bader.budget.server.type.ClosingProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScannedTransactionRepository {

    @Autowired
    private ScannedTransactionMapper scannedTransactionMapper;

    @Autowired
    private ClosingProcessJpaRepository closingProcessJpaRepository;

    @Autowired
    private ScannedTransactionJpaRepository scannedTransactionRepository;

    public List<ScannedTransaction> saveAll(List<ScannedTransaction> transactionList) {
        List<ScannedTransactionDboSql> entities = transactionList.stream()
                                                                 .map(scannedTransactionMapper::mapToOldEntity)
                                                                 .collect(
                                                                         Collectors.toList());
        entities = scannedTransactionRepository.saveAll(entities);
        return entities.stream().map(scannedTransactionMapper::mapToDomain).collect(Collectors.toList());
    }

    public List<ScannedTransaction> getTransactionsForClosingProcess(Integer year, Integer month) {
        ClosingProcessDboSql closingProcessDboSql = closingProcessJpaRepository.findClosingProcessByYearAndMonth(year,
                month);
        if (closingProcessDboSql.getUploadStatus().equals(ClosingProcessStatus.NEW)) {
            return List.of();
        }
        List<ScannedTransactionDboSql> scannedTransactionDboSqls = scannedTransactionRepository.findAllByClosingProcessOrderByDateAsc(
                closingProcessDboSql);
        return scannedTransactionDboSqls.stream()
                                        .map(scannedTransactionMapper::mapToDomain)
                                        .collect(Collectors.toList());
    }

    public List<ScannedTransaction> findAllById(List<String> transactionIds) {
        List<ScannedTransactionDboSql> scannedTransactions = scannedTransactionRepository.findAllById(transactionIds.stream()
                                                                                                                    .map(Integer::parseInt)
                                                                                                                    .collect(
                                                                                                                            Collectors.toList()));
        return scannedTransactions.stream().map(scannedTransactionMapper::mapToDomain).collect(Collectors.toList());
    }
}
