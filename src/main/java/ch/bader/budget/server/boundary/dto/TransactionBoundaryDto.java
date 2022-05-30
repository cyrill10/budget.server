package ch.bader.budget.server.boundary.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionBoundaryDto {
    private String id;
    private VirtualAccountBoundaryDto creditedAccount;
    private VirtualAccountBoundaryDto debitedAccount;
    private LocalDate date;
    private String description;
    private ValueEnumDto paymentStatus;
    private ValueEnumDto indication;
    private ValueEnumDto paymentType;
    private BigDecimal budgetedAmount;
    private BigDecimal effectiveAmount;
    
}
