package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.sql.entity.ClosingProcessDboSql;
import ch.bader.budget.server.adapter.sql.entity.ScannedTransactionDboSql;
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
                                                      .closingProcess(closingProcess)
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
    public void shouldMapDtoToScannedTransaction() {
        //given
        ScannedTransactionBoundaryDto dto = ScannedTransactionBoundaryDto.builder()
                                                                         .id("id")
                                                                         .amount(BigDecimal.TEN)
                                                                         .transactionCreated(true)
                                                                         .cardType(CardType.MasterCard.name())
                                                                         .date(LocalDate.now())
                                                                         .description("scannedTransactionDesc")
                                                                         .build();

        //when
        ScannedTransaction domain = sut.mapToDomain(dto);

        //then
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo("id");
        assertThat(domain.getAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(domain.getTransactionCreated()).isEqualTo(true);
        assertThat(domain.getCardType()).isEqualTo(CardType.MasterCard);
        assertThat(domain.getDate()).isEqualTo(LocalDate.now());
        assertThat(domain.getDescription()).isEqualTo("scannedTransactionDesc");
        assertThat(domain.getClosingProcess()).isNull();
    }

    //
//    @Test
//    public void shouldMapRealAccountToDbo() {
//        //given
//        RealAccount domain = RealAccount.builder()
//                                        .id("id")
//                                        .name("realAccountName")
//                                        .accountType(AccountType.CHECKING)
//                                        .build();
//
//        //when
//        RealAccountDbo account = sut.mapToEntity(domain);
//
//        //then
//        assertThat(account).isNotNull();
//        assertThat(account.getName()).isEqualTo("realAccountName");
//        assertThat(account.getId()).isEqualTo("id");
//        assertThat(account.getAccountType().getValue()).isEqualTo(AccountType.CHECKING.getValue());
//    }
//
//    @Test
//    public void shouldMapDboToRealAccount() {
//        //given
//        RealAccountDbo dbo = RealAccountDbo.builder()
//                                           .id("id")
//                                           .name("realAccountName")
//                                           .accountType(ValueEnumDbo.builder()
//                                                                    .value(AccountType.CHECKING.getValue())
//                                                                    .name(AccountType.CHECKING.getName())
//                                                                    .build())
//                                           .build();
//
//        //when
//        RealAccount account = sut.mapToDomain(dbo);
//
//        //then
//        assertThat(account).isNotNull();
//        assertThat(account.getName()).isEqualTo("realAccountName");
//        assertThat(account.getId()).isEqualTo("id");
//        assertThat(account.getAccountType()).isEqualTo(AccountType.CHECKING);
//    }
//
    @Test
    public void shouldMapScannedTransactionToOldDbo() {
        //given
        ClosingProcess closingProcess = ClosingProcess.builder()
                                                      .id("1")
                                                      .yearMonth(YearMonth.of(2022, 1))
                                                      .manualEntryStatus(ClosingProcessStatus.NEW)
                                                      .uploadStatus(ClosingProcessStatus.DONE)
                                                      .build();

        ScannedTransaction domain = ScannedTransaction.builder()
                                                      .id("6")
                                                      .amount(BigDecimal.TEN)
                                                      .transactionCreated(true)
                                                      .cardType(CardType.MasterCard)
                                                      .date(LocalDate.now())
                                                      .description("scannedTransactionDesc")
                                                      .closingProcess(closingProcess)
                                                      .build();

        //when
        ScannedTransactionDboSql dbo = sut.mapToOldEntity(domain);

        //then
        assertThat(dbo).isNotNull();
        assertThat(dbo.getId()).isEqualTo(6);
        assertThat(dbo.getAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(dbo.getTransactionCreated()).isEqualTo(true);
        assertThat(dbo.getCardType()).isEqualTo(CardType.MasterCard.name());
        assertThat(dbo.getDate()).isEqualTo(LocalDate.now());
        assertThat(dbo.getDescription()).isEqualTo("scannedTransactionDesc");
        assertThat(dbo.getClosingProcess()).isNotNull();
        assertThat(dbo.getClosingProcess().getId()).isEqualTo(1);
        assertThat(dbo.getClosingProcess().getMonth()).isEqualTo(0);
        assertThat(dbo.getClosingProcess().getYear()).isEqualTo(2022);
        assertThat(dbo.getClosingProcess().getManualEntryStatus()).isEqualTo(ClosingProcessStatus.NEW);
        assertThat(dbo.getClosingProcess().getUploadStatus()).isEqualTo(ClosingProcessStatus.DONE);
    }

    @Test
    public void shouldMapOldDboToMapScannedTransaction() {
        //given
        ClosingProcessDboSql closingProcess = ClosingProcessDboSql.builder()
                                                                  .id(1)
                                                                  .year(2022)
                                                                  .month(0)
                                                                  .manualEntryStatus(ClosingProcessStatus.NEW)
                                                                  .uploadStatus(ClosingProcessStatus.DONE)
                                                                  .build();

        ScannedTransactionDboSql dbo = ScannedTransactionDboSql.builder()
                                                               .id(6)
                                                               .amount(BigDecimal.TEN)
                                                               .transactionCreated(true)
                                                               .cardType(CardType.MasterCard.name())
                                                               .date(LocalDate.now())
                                                               .description("scannedTransactionDesc")
                                                               .closingProcess(closingProcess)
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
        assertThat(domain.getClosingProcess()).isNotNull();
        assertThat(domain.getClosingProcess().getId()).isEqualTo("1");
        assertThat(domain.getClosingProcess().getYearMonth()).isEqualTo(YearMonth.of(2022, 1));
        assertThat(domain.getClosingProcess().getManualEntryStatus()).isEqualTo(ClosingProcessStatus.NEW);
        assertThat(domain.getClosingProcess().getUploadStatus()).isEqualTo(ClosingProcessStatus.DONE);
    }
}