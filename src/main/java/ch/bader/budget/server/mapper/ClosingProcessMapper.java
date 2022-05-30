package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.sql.entity.ClosingProcessDboSql;
import ch.bader.budget.server.boundary.dto.ClosingProcessBoundaryDto;
import ch.bader.budget.server.domain.ClosingProcess;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ClosingProcessStatusMapper.class)
public interface ClosingProcessMapper {

    ClosingProcess mapToDomain(ClosingProcessBoundaryDto dto);

    ClosingProcessBoundaryDto mapToDto(ClosingProcess domain);

    ClosingProcessDboSql mapToOldEntity(ClosingProcess domain);

    ClosingProcess mapToDomain(ClosingProcessDboSql entity);
}
