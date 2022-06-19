package ch.bader.budget.server.domain;

import ch.bader.budget.server.type.CardType;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ScannedTransaction implements Comparable<ScannedTransaction> {
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

    @Override
    public int compareTo(@NonNull ScannedTransaction o) {
        if (date.equals(o.date)) {
            return description.compareTo(o.description);
        }
        return date.compareTo(o.date);
    }
}
