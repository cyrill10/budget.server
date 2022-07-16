package ch.bader.budget.server.adapter.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("scannedTransaction")
public class ScannedTransactionDbo {

    @Id
    private String id;
    private String description;
    private LocalDate date;
    private BigDecimal amount;
    private Boolean transactionCreated;
    private String cardType;
    private YearMonth yearMonth;
}
