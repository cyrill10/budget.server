package ch.bader.budget.server.mapper;

import ch.bader.budget.server.boundary.dto.TransactionElementBoundaryDto;
import ch.bader.budget.server.domain.TransactionElement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionElementMapper {

    TransactionElementBoundaryDto mapToDto(TransactionElement domain);
}
