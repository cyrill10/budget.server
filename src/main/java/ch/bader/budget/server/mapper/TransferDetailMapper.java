package ch.bader.budget.server.mapper;

import ch.bader.budget.server.boundary.dto.TransferDetailDto;
import ch.bader.budget.server.domain.TransferDetails;
import org.mapstruct.Mapper;

@Mapper
public interface TransferDetailMapper {

    TransferDetailDto mapToDto(TransferDetails domain);
}
