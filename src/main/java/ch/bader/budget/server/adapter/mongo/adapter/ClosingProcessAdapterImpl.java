package ch.bader.budget.server.adapter.mongo.adapter;

import ch.bader.budget.server.adapter.mongo.entity.ClosingProcessDbo;
import ch.bader.budget.server.adapter.mongo.repository.ClosingProcessMongoRepository;
import ch.bader.budget.server.domain.ClosingProcess;
import ch.bader.budget.server.mapper.ClosingProcessMapper;
import ch.bader.budget.server.repository.ClosingProcessAdapter;
import ch.bader.budget.server.type.ClosingProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service("closingProcessMongo")
public class ClosingProcessAdapterImpl implements ClosingProcessAdapter {

    @Autowired
    private ClosingProcessMongoRepository closingProcessMongoRepository;

    @Autowired
    ClosingProcessMapper closingProcessMapper;

    @Override
    public ClosingProcess getClosingProcess(YearMonth yearMonth) {
        ClosingProcessDbo dbo = closingProcessMongoRepository.findByYearMonth(yearMonth);
        if (dbo == null) {
            ClosingProcess newClosingProcess = ClosingProcess.builder()
                                                             .yearMonth(yearMonth)
                                                             .manualEntryStatus(ClosingProcessStatus.NEW)
                                                             .uploadStatus(ClosingProcessStatus.NEW)
                                                             .build();
            dbo = closingProcessMongoRepository.save(closingProcessMapper.mapToEntity(newClosingProcess));
        }
        return closingProcessMapper.mapToDomain(dbo);
    }

    @Override
    public ClosingProcess save(ClosingProcess closingProcess) {
        ClosingProcessDbo dbo = closingProcessMongoRepository.save(closingProcessMapper.mapToEntity(closingProcess));
        return closingProcessMapper.mapToDomain(dbo);
    }
}
