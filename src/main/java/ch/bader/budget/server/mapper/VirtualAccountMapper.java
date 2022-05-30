package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.sql.entity.VirtualAccountDboSql;
import ch.bader.budget.server.boundary.dto.VirtualAccountBoundaryDto;
import ch.bader.budget.server.domain.VirtualAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = RealAccountMapper.class)
public interface VirtualAccountMapper {

    VirtualAccount mapToDomain(VirtualAccountBoundaryDto dto);

    VirtualAccountBoundaryDto mapToDto(VirtualAccount domain);

    VirtualAccountDboSql mapToOldEntity(VirtualAccount domain);
    
    VirtualAccount mapToDomain(VirtualAccountDboSql entity);
}
