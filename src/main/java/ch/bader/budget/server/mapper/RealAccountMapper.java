package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.cosmos.entity.RealAccountDbo;
import ch.bader.budget.server.adapter.sql.entity.RealAccountDboSql;
import ch.bader.budget.server.boundary.dto.RealAccountBoundaryDto;
import ch.bader.budget.server.domain.RealAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AccountTypeMapper.class)
public interface RealAccountMapper {

    RealAccount mapToDomain(RealAccountBoundaryDto dto);

    RealAccountBoundaryDto mapToDto(RealAccount domain);

    RealAccountDbo mapToEntity(RealAccount domain);

    RealAccountDboSql mapToOldEntity(RealAccount domain);

    RealAccount mapToDomain(RealAccountDboSql entity);

    RealAccount mapToDomain(RealAccountDbo entity);
}
