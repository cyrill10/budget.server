package ch.bader.budget.server.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SaveScannedTransactionBoundaryDto {

    List<String> transactionIds;
    String creditedAccountId;
    String debitedAccountId;
    String throughAccountId;

}
