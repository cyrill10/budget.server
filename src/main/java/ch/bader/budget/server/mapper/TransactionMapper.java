package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.sql.entity.TransactionDboSql;
import ch.bader.budget.server.boundary.dto.TransactionBoundaryDto;
import ch.bader.budget.server.domain.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {VirtualAccountMapper.class, PaymentStatusMapper.class, PaymentTypeMapper.class, TransactionIndicationMapper.class})
public interface TransactionMapper {

    Transaction mapToDomain(TransactionBoundaryDto dto);

    TransactionBoundaryDto mapToDto(Transaction domain);

    TransactionDboSql mapToOldEntity(Transaction domain);

    Transaction mapToDomain(TransactionDboSql entity);
}
