package ch.bader.budget.server.boundary;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SaveScannedTransactionBoundaryDto {

    List<Integer> transactionIds;
    Integer creditedAccountId;
    Integer debitedAccountId;
    Integer throughAccountId;

}
