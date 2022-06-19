package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.ScannedTransactionDbo;
import ch.bader.budget.server.boundary.dto.ScannedTransactionBoundaryDto;
import ch.bader.budget.server.domain.ClosingProcess;
import ch.bader.budget.server.domain.ScannedTransaction;
import ch.bader.budget.server.type.CardType;
import ch.bader.budget.server.type.ClosingProcessStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ScannedTransactionMapperTest {

    @Autowired
    private ScannedTransactionMapperImpl sut;

    @Test
    public void shouldMapScannedTransactionToDto() {
        //given

        ClosingProcess closingProcess = ClosingProcess.builder()
                                                      .id("id")
                                                      .yearMonth(YearMonth.of(2022, 1))
                                                      .manualEntryStatus(ClosingProcessStatus.NEW)
                                                      .uploadStatus(ClosingProcessStatus.DONE)
                                                      .build();

        ScannedTransaction domain = ScannedTransaction.builder()
                                                      .id("id")
                                                      .amount(BigDecimal.TEN)
                                                      .transactionCreated(true)
                                                      .cardType(CardType.MasterCard)
                                                      .date(LocalDate.now())
                                                      .description("scannedTransactionDesc")
                                                      .yearMonth(YearMonth.of(2022, 1))
                                                      .build();

        //when
        ScannedTransactionBoundaryDto dto = sut.mapToDto(domain);

        //then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("id");
        assertThat(dto.getAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(dto.getTransactionCreated()).isEqualTo(true);
        assertThat(dto.getCardType()).isEqualTo(CardType.MasterCard.name());
        assertThat(dto.getDate()).isEqualTo(LocalDate.now());
        assertThat(dto.getDescription()).isEqualTo("scannedTransactionDesc");
    }

    @Test
    public void shouldMapScannedTransactionToDbo() {
        //given
        ScannedTransaction domain = ScannedTransaction.builder()
                                                      .id("6")
                                                      .amount(BigDecimal.TEN)
                                                      .transactionCreated(true)
                                                      .cardType(CardType.MasterCard)
                                                      .date(LocalDate.now())
                                                      .description("scannedTransactionDesc")
                                                      .yearMonth(YearMonth.of(2022, 1))
                                                      .build();

        //when
        ScannedTransactionDbo dbo = sut.mapToEntity(domain);

        //then
        assertThat(dbo).isNotNull();
        assertThat(dbo.getId()).isEqualTo("6");
        assertThat(dbo.getAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(dbo.getTransactionCreated()).isEqualTo(true);
        assertThat(dbo.getCardType()).isEqualTo(CardType.MasterCard.name());
        assertThat(dbo.getDate()).isEqualTo(LocalDate.now());
        assertThat(dbo.getDescription()).isEqualTo("scannedTransactionDesc");
        assertThat(dbo.getYearMonth()).isEqualTo(YearMonth.of(2022, 1));
    }

    @Test
    public void shouldMapDboToRealAccount() {
        //given
        ScannedTransactionDbo dbo = ScannedTransactionDbo.builder()
                                                         .id("6")
                                                         .amount(BigDecimal.TEN)
                                                         .transactionCreated(true)
                                                         .cardType(CardType.MasterCard.name())
                                                         .date(LocalDate.now())
                                                         .description("scannedTransactionDesc")
                                                         .yearMonth(YearMonth.of(2022, 1))
                                                         .build();

        //when
        ScannedTransaction domain = sut.mapToDomain(dbo);

        //then
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo("6");
        assertThat(domain.getAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(domain.getTransactionCreated()).isEqualTo(true);
        assertThat(domain.getCardType()).isEqualTo(CardType.MasterCard);
        assertThat(domain.getDate()).isEqualTo(LocalDate.now());
        assertThat(domain.getDescription()).isEqualTo("scannedTransactionDesc");
        assertThat(domain.getYearMonth()).isEqualTo(YearMonth.of(2022, 1));
    }

}