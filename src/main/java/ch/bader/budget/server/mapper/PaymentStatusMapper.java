package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.ValueEnumDbo;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.type.PaymentStatus;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentStatusMapper {

    ValueEnumDbo mapToDbo(PaymentStatus domain);

    ValueEnumDto mapToDto(PaymentStatus domain);

    default PaymentStatus mapToDomain(ValueEnumDbo entity) {
        return PaymentStatus.forValue(entity.getValue());
    }

    default PaymentStatus mapToDomain(ValueEnumDto dto) {
        return PaymentStatus.forValue(dto.getValue());
    }
}