package ch.bader.budget.server.repository;

import ch.bader.budget.server.adapter.sql.entity.ClosingProcessDboSql;
import ch.bader.budget.server.adapter.sql.repository.ClosingProcessJpaRepository;
import ch.bader.budget.server.domain.ClosingProcess;
import ch.bader.budget.server.mapper.ClosingProcessMapper;
import ch.bader.budget.server.type.ClosingProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClosingProcessRepository {

    @Autowired
    private ClosingProcessJpaRepository closingProcessRepository;

    @Autowired
    ClosingProcessMapper closingProcessMapper;

    public ClosingProcess getClosingProcess(Integer year, Integer month) {
        ClosingProcessDboSql result = closingProcessRepository.findClosingProcessByYearAndMonth(year, month);
        if (result == null) {
            ClosingProcessDboSql newClosingProcess = ClosingProcessDboSql.builder()
                                                                         .year(year)
                                                                         .month(month)
                                                                         .manualEntryStatus(ClosingProcessStatus.NEW)
                                                                         .uploadStatus(ClosingProcessStatus.NEW)
                                                                         .creationDate(LocalDateTime.now())
                                                                         .build();
            result = closingProcessRepository.save(newClosingProcess);
        }
        return closingProcessMapper.mapToDomain(result);
    }

    public ClosingProcess save(ClosingProcess closingProcess) {
        ClosingProcessDboSql closingProcessDboSql = closingProcessMapper.mapToOldEntity(closingProcess);
        closingProcessDboSql = closingProcessRepository.save(closingProcessDboSql);
        return closingProcessMapper.mapToDomain(closingProcessDboSql);
    }
}
