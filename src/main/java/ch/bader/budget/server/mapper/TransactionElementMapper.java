package ch.bader.budget.server.mapper;

import ch.bader.budget.server.boundary.dto.TransactionElementBoundaryDto;
import ch.bader.budget.server.domain.TransactionListElement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransactionElementMapper {

    @Mapping(target = "amount", source = "effectiveAmount")
    @Mapping(target = "balance", source = "effectiveBalance")
    TransactionElementBoundaryDto mapToDto(TransactionListElement domain);
}
