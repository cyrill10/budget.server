package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.TransactionDbo;
import ch.bader.budget.server.boundary.dto.TransactionBoundaryDto;
import ch.bader.budget.server.domain.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {VirtualAccountMapper.class, PaymentStatusMapper.class, PaymentTypeMapper.class, TransactionIndicationMapper.class})
public interface TransactionMapper {

    Transaction mapToDomain(TransactionBoundaryDto dto);

    TransactionBoundaryDto mapToDto(Transaction domain);
    
    Transaction mapToDomain(TransactionDbo entity);

    @Mapping(target = "creditedAccountId", source = "creditedAccount.id")
    @Mapping(target = "debitedAccountId", source = "debitedAccount.id")
    TransactionDbo mapToEntity(Transaction domain);
}
