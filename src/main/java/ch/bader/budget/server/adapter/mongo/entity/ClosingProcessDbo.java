package ch.bader.budget.server.adapter.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("closingProcess")
public class ClosingProcessDbo {

    @Id
    private String id;
    private YearMonth yearMonth;
    private ValueEnumDbo uploadStatus;
    private ValueEnumDbo manualEntryStatus;
    private ValueEnumDbo transferStatus;

}
