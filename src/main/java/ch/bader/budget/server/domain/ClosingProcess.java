package ch.bader.budget.server.domain;

import ch.bader.budget.server.type.ClosingProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.YearMonth;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClosingProcess implements Comparable<ClosingProcess> {

    private String id;
    YearMonth yearMonth;
    private ClosingProcessStatus uploadStatus;
    private ClosingProcessStatus manualEntryStatus;

    @Override
    public int compareTo(@NonNull ClosingProcess o) {
        return yearMonth.compareTo(o.yearMonth);
    }
}
