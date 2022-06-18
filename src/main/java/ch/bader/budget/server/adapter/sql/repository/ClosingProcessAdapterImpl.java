package ch.bader.budget.server.adapter.sql.repository;

import ch.bader.budget.server.adapter.sql.entity.ClosingProcessDboSql;
import ch.bader.budget.server.adapter.sql.repository.jpa.ClosingProcessJpaRepository;
import ch.bader.budget.server.domain.ClosingProcess;
import ch.bader.budget.server.mapper.ClosingProcessMapper;
import ch.bader.budget.server.repository.ClosingProcessAdapter;
import ch.bader.budget.server.type.ClosingProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Component("closingProcessMySql")
public class ClosingProcessAdapterImpl implements ClosingProcessAdapter {

    @Autowired
    private ClosingProcessJpaRepository closingProcessRepository;

    @Autowired
    ClosingProcessMapper closingProcessMapper;

    @Override
    public ClosingProcess getClosingProcess(YearMonth yearMonth) {

        ClosingProcessDboSql result = closingProcessRepository.findClosingProcessByYearAndMonth(yearMonth.getYear(),
            yearMonth.getMonthValue() - 1);
        if (result == null) {
            ClosingProcessDboSql newClosingProcess = ClosingProcessDboSql.builder()
                                                                         .year(yearMonth.getYear())
                                                                         .month(yearMonth.getMonthValue() - 1)
                                                                         .manualEntryStatus(ClosingProcessStatus.NEW)
                                                                         .uploadStatus(ClosingProcessStatus.NEW)
                                                                         .creationDate(LocalDateTime.now())
                                                                         .build();
            result = closingProcessRepository.save(newClosingProcess);
        }
        return closingProcessMapper.mapToDomain(result);
    }

    @Override
    public ClosingProcess save(ClosingProcess closingProcess) {
        ClosingProcessDboSql closingProcessDboSql = closingProcessMapper.mapToOldEntity(closingProcess);
        closingProcessDboSql = closingProcessRepository.save(closingProcessDboSql);
        return closingProcessMapper.mapToDomain(closingProcessDboSql);
    }
}
