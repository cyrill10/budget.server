package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.ValueEnumDbo;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.type.PaymentType;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentTypeMapper {

    ValueEnumDbo mapToDbo(PaymentType domain);

    ValueEnumDto mapToDto(PaymentType domain);

    default PaymentType mapToDomain(ValueEnumDbo entity) {
        return PaymentType.forValue(entity.getValue());
    }

    default PaymentType mapToDomain(ValueEnumDto dto) {
        return PaymentType.forValue(dto.getValue());
    }
}