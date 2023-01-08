package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.RealAccountDbo;
import ch.bader.budget.server.boundary.dto.RealAccountBoundaryDto;
import ch.bader.budget.server.domain.RealAccount;
import org.mapstruct.Mapper;

@Mapper(uses = AccountTypeMapper.class)
public interface RealAccountMapper {

    RealAccount mapToDomain(RealAccountBoundaryDto dto);

    RealAccountBoundaryDto mapToDto(RealAccount domain);

    RealAccountDbo mapToEntity(RealAccount domain);

    RealAccount mapToDomain(RealAccountDbo entity);
}
