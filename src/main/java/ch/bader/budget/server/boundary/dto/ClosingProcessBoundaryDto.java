package ch.bader.budget.server.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClosingProcessBoundaryDto {

    private String id;
    private Integer year;
    private Integer month;
    private ValueEnumDto uploadStatus;
    private ValueEnumDto manualEntryStatus;
}
