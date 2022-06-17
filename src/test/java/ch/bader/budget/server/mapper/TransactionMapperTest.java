package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.TransactionDbo;
import ch.bader.budget.server.adapter.mongo.entity.ValueEnumDbo;
import ch.bader.budget.server.boundary.dto.RealAccountBoundaryDto;
import ch.bader.budget.server.boundary.dto.TransactionBoundaryDto;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.boundary.dto.VirtualAccountBoundaryDto;
import ch.bader.budget.server.domain.RealAccount;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.type.AccountType;
import ch.bader.budget.server.type.PaymentStatus;
import ch.bader.budget.server.type.PaymentType;
import ch.bader.budget.server.type.TransactionIndication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TransactionMapperTest {

    @Autowired
    private TransactionMapperImpl sut;

    @Test
    public void shouldMapTransactionToDto() {
        //given
        RealAccount realAccount = RealAccount
            .builder()
            .id("id")
            .name("realAccountName")
            .accountType(AccountType.CHECKING)
            .build();

        VirtualAccount virtualAccount = VirtualAccount
            .builder()
            .id("idVirtual")
            .name("virtualAccountName")
            .balance(BigDecimal.ZERO)
            .isDeleted(false)
            .underlyingAccount(realAccount)
            .build();

        LocalDate date = LocalDate.now();
        LocalDateTime dateTime = LocalDateTime.now();

        Transaction transaction = Transaction
            .builder()
            .id("idTransaction")
            .date(date)
            .paymentType(PaymentType.DEPOSIT)
            .paymentStatus(PaymentStatus.PAID)
            .indication(
                TransactionIndication.EXPECTED)
            .description("newTransaction")
            .debitedAccount(virtualAccount)
            .creditedAccount(virtualAccount)
            .effectiveAmount(BigDecimal.TEN)
            .budgetedAmount(BigDecimal.ONE)
            .creationDate(dateTime)
            .build();

        //when
        TransactionBoundaryDto transactionBoundaryDto = sut.mapToDto(transaction);

        //then
        assertThat(transactionBoundaryDto).isNotNull();
        assertThat(transactionBoundaryDto.getDescription()).isEqualTo("newTransaction");
        assertThat(transactionBoundaryDto.getId()).isEqualTo("idTransaction");
        assertThat(transactionBoundaryDto.getDate()).isEqualTo(date);
        assertThat(transactionBoundaryDto.getBudgetedAmount()).isEqualTo(BigDecimal.ONE);
        assertThat(transactionBoundaryDto.getEffectiveAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(transactionBoundaryDto
            .getIndication()
            .getValue()).isEqualTo(TransactionIndication.EXPECTED.getValue());
        assertThat(transactionBoundaryDto
            .getPaymentStatus()
            .getValue()).isEqualTo(PaymentStatus.PAID.getValue());
        assertThat(transactionBoundaryDto
            .getPaymentType()
            .getValue()).isEqualTo(PaymentType.DEPOSIT.getValue());
        assertThat(transactionBoundaryDto.getCreationDate()).isEqualTo(dateTime);
        assertThat(transactionBoundaryDto.getCreditedAccount()
                                         .getName()).isEqualTo("virtualAccountName");
        assertThat(transactionBoundaryDto.getDebitedAccount()
                                         .getName()).isEqualTo("virtualAccountName");
    }

    @Test
    public void shouldMapDtoToTransaction() {
        //given
        RealAccountBoundaryDto realAccount = RealAccountBoundaryDto
            .builder()
            .id("id")
            .name("realAccountName")
            .accountType(ValueEnumDto.builder().value(AccountType.CHECKING.getValue()).build())
            .build();

        VirtualAccountBoundaryDto virtualAccount = VirtualAccountBoundaryDto
            .builder()
            .id("idVirtual")
            .name("virtualAccountName")
            .balance(BigDecimal.ZERO)
            .isDeleted(false)
            .underlyingAccount(realAccount)
            .build();

        LocalDate date = LocalDate.now();
        LocalDateTime dateTime = LocalDateTime.now();

        TransactionBoundaryDto dto = TransactionBoundaryDto
            .builder()
            .id("idTransaction")
            .date(date)
            .paymentType(ValueEnumDto.builder().value(PaymentType.DEPOSIT.getValue()).build())
            .paymentStatus(ValueEnumDto.builder().value(PaymentStatus.PAID.getValue()).build())
            .indication(ValueEnumDto.builder().value(TransactionIndication.EXPECTED.getValue()).build())
            .description("newTransaction")
            .debitedAccount(virtualAccount)
            .creditedAccount(virtualAccount)
            .effectiveAmount(BigDecimal.TEN)
            .budgetedAmount(BigDecimal.ONE)
            .creationDate(dateTime)
            .build();

        //when
        Transaction transaction = sut.mapToDomain(dto);

        //then
        assertThat(transaction).isNotNull();
        assertThat(transaction.getDescription()).isEqualTo("newTransaction");
        assertThat(transaction.getId()).isEqualTo("idTransaction");
        assertThat(transaction.getDate()).isEqualTo(date);
        assertThat(transaction.getBudgetedAmount()).isEqualTo(BigDecimal.ONE);
        assertThat(transaction.getEffectiveAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(transaction
            .getIndication()
            .getValue()).isEqualTo(TransactionIndication.EXPECTED.getValue());
        assertThat(transaction
            .getPaymentStatus()
            .getValue()).isEqualTo(PaymentStatus.PAID.getValue());
        assertThat(transaction
            .getPaymentType()
            .getValue()).isEqualTo(PaymentType.DEPOSIT.getValue());
        assertThat(transaction.getCreationDate()).isEqualTo(dateTime);
        assertThat(transaction.getCreditedAccount()
                              .getName()).isEqualTo("virtualAccountName");
        assertThat(transaction.getDebitedAccount()
                              .getName()).isEqualTo("virtualAccountName");
    }

    @Test
    public void shouldMapTransactionToDbo() {
        //given
        RealAccount realAccount = RealAccount
            .builder()
            .id("id")
            .name("realAccountName")
            .accountType(AccountType.CHECKING)
            .build();

        VirtualAccount virtualAccount = VirtualAccount
            .builder()
            .id("idVirtual")
            .name("virtualAccountName")
            .balance(BigDecimal.ZERO)
            .isDeleted(false)
            .underlyingAccount(realAccount)
            .build();

        LocalDate date = LocalDate.now();
        LocalDateTime dateTime = LocalDateTime.now();

        Transaction domain = Transaction
            .builder()
            .id("idTransaction")
            .date(date)
            .paymentType(PaymentType.DEPOSIT)
            .paymentStatus(PaymentStatus.PAID)
            .indication(
                TransactionIndication.EXPECTED)
            .description("newTransaction")
            .debitedAccount(virtualAccount)
            .creditedAccount(virtualAccount)
            .effectiveAmount(BigDecimal.TEN)
            .budgetedAmount(BigDecimal.ONE)
            .creationDate(dateTime)
            .build();

        //when
        TransactionDbo transaction = sut.mapToEntity(domain);

        //then
        assertThat(transaction).isNotNull();
        assertThat(transaction.getDescription()).isEqualTo("newTransaction");
        assertThat(transaction.getId()).isEqualTo("idTransaction");
        assertThat(transaction.getDate()).isEqualTo(date);
        assertThat(transaction.getBudgetedAmount()).isEqualTo(BigDecimal.ONE);
        assertThat(transaction.getEffectiveAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(transaction
            .getIndication()
            .getValue()).isEqualTo(TransactionIndication.EXPECTED.getValue());
        assertThat(transaction
            .getPaymentStatus()
            .getValue()).isEqualTo(PaymentStatus.PAID.getValue());
        assertThat(transaction
            .getPaymentType()
            .getValue()).isEqualTo(PaymentType.DEPOSIT.getValue());
        assertThat(transaction.getCreationDate()).isEqualTo(dateTime);
        assertThat(transaction.getCreditedAccountId()).isEqualTo("idVirtual");
        assertThat(transaction.getDebitedAccountId()).isEqualTo("idVirtual");

    }

    @Test
    public void shouldMapDboToRealAccount() {
        //given
        LocalDate date = LocalDate.now();
        LocalDateTime dateTime = LocalDateTime.now();

        TransactionDbo dbo = TransactionDbo
            .builder()
            .id("idTransaction")
            .date(date)
            .paymentType(ValueEnumDbo.builder().value(PaymentType.DEPOSIT.getValue()).build())
            .paymentStatus(ValueEnumDbo.builder().value(PaymentStatus.PAID.getValue()).build())
            .indication(ValueEnumDbo.builder().value(TransactionIndication.EXPECTED.getValue()).build())
            .description("newTransaction")
            .debitedAccountId("virtualAccountId")
            .creditedAccountId("virtualAccountId")
            .effectiveAmount(BigDecimal.TEN)
            .budgetedAmount(BigDecimal.ONE)
            .creationDate(dateTime)
            .build();

        //when
        Transaction transaction = sut.mapToDomain(dbo);

        //then
        assertThat(transaction).isNotNull();
        assertThat(transaction.getDescription()).isEqualTo("newTransaction");
        assertThat(transaction.getId()).isEqualTo("idTransaction");
        assertThat(transaction.getDate()).isEqualTo(date);
        assertThat(transaction.getBudgetedAmount()).isEqualTo(BigDecimal.ONE);
        assertThat(transaction.getEffectiveAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(transaction
            .getIndication()
            .getValue()).isEqualTo(TransactionIndication.EXPECTED.getValue());
        assertThat(transaction
            .getPaymentStatus()
            .getValue()).isEqualTo(PaymentStatus.PAID.getValue());
        assertThat(transaction
            .getPaymentType()
            .getValue()).isEqualTo(PaymentType.DEPOSIT.getValue());
        assertThat(transaction.getCreationDate()).isEqualTo(dateTime);
    }
}