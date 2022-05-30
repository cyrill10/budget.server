package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.cosmos.entity.ValueEnumDbo;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.type.TransactionIndication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionIndicationMapper {

    ValueEnumDbo mapToDbo(TransactionIndication domain);

    ValueEnumDto mapToDto(TransactionIndication domain);

    default TransactionIndication mapToDomain(ValueEnumDbo entity) {
        return TransactionIndication.forValue(entity.getValue());
    }

    default TransactionIndication mapToDomain(ValueEnumDto dto) {
        return TransactionIndication.forValue(dto.getValue());
    }
}