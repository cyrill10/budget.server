package ch.bader.budget.server.mapper;

import ch.bader.budget.server.type.CardType;
import org.mapstruct.Mapper;

@Mapper
public interface CardTypeMapper {

    default String mapToString(CardType domain) {
        return domain.name();
    }

    default CardType mapToDomain(String name) {
        return CardType.valueOf(name);
    }
}