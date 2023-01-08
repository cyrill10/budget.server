package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.ScannedTransactionDbo;
import ch.bader.budget.server.boundary.dto.ScannedTransactionBoundaryDto;
import ch.bader.budget.server.domain.ScannedTransaction;
import org.mapstruct.Mapper;

import java.time.YearMonth;

@Mapper(uses = {CardTypeMapper.class, ClosingProcessMapper.class}, imports = YearMonth.class)
public interface ScannedTransactionMapper {

    ScannedTransactionBoundaryDto mapToDto(ScannedTransaction domain);

    ScannedTransaction mapToDomain(ScannedTransactionDbo entity);

    ScannedTransactionDbo mapToEntity(ScannedTransaction domain);
}
