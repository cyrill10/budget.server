package ch.bader.budget.server.boundary.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValueEnumDto {
    private String name;
    private Integer value;
}
