package ch.bader.budget.server.process.closing;

import ch.bader.budget.server.entity.ClosingProcess;
import ch.bader.budget.server.entity.ScannedTransaction;
import ch.bader.budget.server.type.CardType;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ScannedTransactionBean {

    @CsvBindByName(column = "Transaction date", required = true)
    @CsvDate("dd.MM.yyyy")
    private LocalDate transactionDate;

    @CsvBindByName(column = "Description", required = true)
    private String description;

    @CsvBindByName(column = "Card number", required = true)
    private String cardNumber;

    @CsvBindByName(column = "Amount", required = true)
    @CsvNumber("#.##")
    private BigDecimal amount;

    @CsvBindByName(column = "Debit/Credit", required = true)
    private String type;

    public ScannedTransaction mapTopScannedTransaction(ClosingProcess closingProcess) {
        return ScannedTransaction.builder()
                                 .creationDate(LocalDateTime.now())
                                 .date(transactionDate)
                                 .transactionCreated(false)
                                 .amount(amount)
                                 .cardType(getCardType())
                                 .closingProcess(closingProcess)
                                 .description(description)
                                 .build();
    }

    public CardType getCardType() {
        return cardNumber.endsWith("9709") ? CardType.AMEX : CardType.MasterCard;
    }

}
