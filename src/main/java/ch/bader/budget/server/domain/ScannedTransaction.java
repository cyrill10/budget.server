package ch.bader.budget.server.domain;

import ch.bader.budget.server.type.CardType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ScannedTransaction {
    private String id;
    private String description;
    private LocalDate date;
    private BigDecimal amount;
    private Boolean transactionCreated;
    private CardType cardType;
    private ClosingProcess closingProcess;

    public ScannedTransaction createTransaction() {
        this.setTransactionCreated(true);
        return this;
    }
}
