package ch.bader.budget.server.mapper;

import ch.bader.budget.server.boundary.dto.OverviewElementBoundaryDto;
import ch.bader.budget.server.domain.OverviewElement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OverviewElementMapper {

    OverviewElementBoundaryDto mapToDto(OverviewElement domain);
}
