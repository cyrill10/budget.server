package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.ValueEnumDbo;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.type.AccountType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountTypeMapper {

    ValueEnumDbo mapToDbo(AccountType domain);

    ValueEnumDto mapToDto(AccountType domain);

    default AccountType mapToDomain(ValueEnumDbo entity) {
        return AccountType.forValue(entity.getValue());
    }

    default AccountType mapToDomain(ValueEnumDto dto) {
        return AccountType.forValue(dto.getValue());
    }
}