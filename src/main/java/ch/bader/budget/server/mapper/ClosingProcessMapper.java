package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.ClosingProcessDbo;
import ch.bader.budget.server.boundary.dto.ClosingProcessBoundaryDto;
import ch.bader.budget.server.domain.ClosingProcess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.YearMonth;

@Mapper(componentModel = "spring", uses = ClosingProcessStatusMapper.class, imports = YearMonth.class)
public interface ClosingProcessMapper {

    @Mapping(target = "year", expression = "java(domain.getYearMonth().getYear())")
    @Mapping(target = "month", expression = "java(domain.getYearMonth().getMonthValue()-1)")
    ClosingProcessBoundaryDto mapToDto(ClosingProcess domain);

    ClosingProcess mapToDomain(ClosingProcessDbo entity);

    ClosingProcessDbo mapToEntity(ClosingProcess domain);
}
