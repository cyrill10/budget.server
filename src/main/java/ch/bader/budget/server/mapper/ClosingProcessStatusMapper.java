package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.ValueEnumDbo;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.type.ClosingProcessStatus;
import org.mapstruct.Mapper;

@Mapper
public interface ClosingProcessStatusMapper {

    ValueEnumDbo mapToDbo(ClosingProcessStatus domain);

    ValueEnumDto mapToDto(ClosingProcessStatus domain);

    default ClosingProcessStatus mapToDomain(ValueEnumDbo entity) {
        if (entity != null) {
            return ClosingProcessStatus.forValue(entity.getValue());
        }
        return ClosingProcessStatus.NEW;
    }

}