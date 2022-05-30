package ch.bader.budget.server.domain;

import ch.bader.budget.server.type.PaymentStatus;
import ch.bader.budget.server.type.PaymentType;
import ch.bader.budget.server.type.TransactionIndication;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class Transaction implements Comparable<Transaction> {
    private String id;
    private VirtualAccount creditedAccount;
    private VirtualAccount debitedAccount;
    private LocalDate date;
    private String description;
    private PaymentStatus paymentStatus;
    private TransactionIndication indication;
    private PaymentType paymentType;
    private BigDecimal budgetedAmount;
    private BigDecimal effectiveAmount;

    public Transaction createDuplicate(LocalDate newDate) {
        return Transaction.builder()
                          .budgetedAmount(this.budgetedAmount)
                          .effectiveAmount(this.effectiveAmount)
                          .creditedAccount(this.creditedAccount)
                          .debitedAccount(this.debitedAccount)
                          .description(this.description)
                          .paymentStatus(this.paymentStatus)
                          .indication(this.indication)
                          .paymentType(this.paymentType)
                          .date(newDate)
                          .build();
    }

    @Override
    public int compareTo(Transaction o) {
        return this.id.compareTo(o.id);

    }
}
