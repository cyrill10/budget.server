package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.sql.entity.ScannedTransactionDboSql;
import ch.bader.budget.server.boundary.dto.ScannedTransactionBoundaryDto;
import ch.bader.budget.server.domain.ScannedTransaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CardTypeMapper.class)
public interface ScannedTransactionMapper {

    ScannedTransaction mapToDomain(ScannedTransactionBoundaryDto dto);

    ScannedTransactionBoundaryDto mapToDto(ScannedTransaction domain);

    ScannedTransactionDboSql mapToOldEntity(ScannedTransaction domain);

    ScannedTransaction mapToDomain(ScannedTransactionDboSql entity);
}
