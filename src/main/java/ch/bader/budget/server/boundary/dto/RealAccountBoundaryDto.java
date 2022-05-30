package ch.bader.budget.server.boundary.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RealAccountBoundaryDto {

    private String id;
    private String name;
    private ValueEnumDto accountType;
}
