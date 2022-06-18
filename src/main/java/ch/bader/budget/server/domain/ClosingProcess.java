package ch.bader.budget.server.domain;

import ch.bader.budget.server.type.ClosingProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClosingProcess {

    private String id;
    YearMonth yearMonth;
    private ClosingProcessStatus uploadStatus;
    private ClosingProcessStatus manualEntryStatus;
}
