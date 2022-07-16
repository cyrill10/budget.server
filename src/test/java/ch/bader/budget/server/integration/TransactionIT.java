package ch.bader.budget.server.integration;

import ch.bader.budget.server.boundary.dto.RealAccountBoundaryDto;
import ch.bader.budget.server.boundary.dto.TransactionBoundaryDto;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.boundary.dto.VirtualAccountBoundaryDto;
import ch.bader.budget.server.type.AccountType;
import ch.bader.budget.server.type.PaymentStatus;
import ch.bader.budget.server.type.PaymentType;
import ch.bader.budget.server.type.TransactionIndication;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;

import static ch.bader.budget.server.TestUtils.asJsonString;
import static ch.bader.budget.server.TestUtils.getAuthHeader;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionIT extends AbstractIT {

    @Test
    public void shouldLoadDb() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();
    }

    @Test
    public void shouldAddTransaction() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();

        RealAccountBoundaryDto underlyingAccount = RealAccountBoundaryDto
            .builder()
            //4
            .id("62aa325d44240b637b194243")
            .name("Credit Cards")
            .accountType(ValueEnumDto.builder()
                                     .value(AccountType.CREDIT.getValue())
                                     .build())
            .build();

        VirtualAccountBoundaryDto virtualAccount = VirtualAccountBoundaryDto
            .builder()
            //2
            .id("62d172d93b2f355e5ceafb63")
            .name("Bonviva")
            .isDeleted(false).balance(BigDecimal.ZERO)
            .underlyingAccount(underlyingAccount)
            .build();

        TransactionBoundaryDto input = TransactionBoundaryDto
            .builder()
            .creditedAccount(virtualAccount)
            .debitedAccount(virtualAccount)
            .description("Test Transaction")
            .date(LocalDate.now())
            .paymentType(ValueEnumDto.builder()
                                     .value(PaymentType.DEPOSIT.getValue())
                                     .build())
            .paymentStatus(ValueEnumDto.builder()
                                       .value(PaymentStatus.PAID.getValue())
                                       .build())
            .indication(ValueEnumDto.builder()
                                    .value(TransactionIndication.EXPECTED.getValue())
                                    .build())
            .budgetedAmount(BigDecimal.ONE)
            .effectiveAmount(BigDecimal.TEN)
            .build();


        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .body(asJsonString(input))
               .when()
               .post("/budget/transaction/add/")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_CREATED)
               .body("description", equalTo("Test Transaction"))
               .body("id", isA(String.class))
               .body("budgetedAmount", equalTo(1))
               .body("effectiveAmount", equalTo(10))
               .body("debitedAccount.name", equalTo("Bonviva"))
               .body("creditedAccount.name", equalTo("Bonviva"))
               .body("debitedAccount.underlyingAccount.accountType.value", equalTo(AccountType.CREDIT.getValue()));
    }

    @Test
    public void shouldUpdateTransaction() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();

        RealAccountBoundaryDto underlyingAccount = RealAccountBoundaryDto
            .builder()
            //1
            .id("62d172d23b2f355e5ceafb5a")
            .name("Checking")
            .accountType(ValueEnumDto.builder()
                                     .value(AccountType.CHECKING.getValue())
                                     .build())
            .build();

        VirtualAccountBoundaryDto virtualAccountChecking = VirtualAccountBoundaryDto
            .builder()
            //4
            .id("62d172d93b2f355e5ceafb65")
            .name("Checking")
            .isDeleted(false).balance(BigDecimal.ZERO)
            .underlyingAccount(underlyingAccount)
            .build();

        VirtualAccountBoundaryDto virtualAccountHealth = VirtualAccountBoundaryDto
            .builder()
            //7
            .id("62d172d93b2f355e5ceafb68")
            .name("Health Savings")
            .isDeleted(false).balance(BigDecimal.ZERO)
            .underlyingAccount(underlyingAccount)
            .build();


        TransactionBoundaryDto input = TransactionBoundaryDto
            .builder()
            //294
            .id("62d172e63b2f355e5ceafc9b")
            .creditedAccount(virtualAccountChecking)
            .debitedAccount(virtualAccountHealth)
            .description("Health Saving")
            .date(LocalDate.of(2022, 2, 25))
            .paymentType(ValueEnumDto.builder()
                                     .value(PaymentType.DEPOSIT.getValue())
                                     .build())
            .paymentStatus(ValueEnumDto.builder()
                                       .value(PaymentStatus.PAID.getValue())
                                       .build())
            .indication(ValueEnumDto.builder()
                                    .value(TransactionIndication.EXPECTED.getValue())
                                    .build())
            .budgetedAmount(BigDecimal.valueOf(400))
            .effectiveAmount(BigDecimal.valueOf(500))
            .build();

        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .body(asJsonString(input))
               .when()
               .put("/budget/transaction/update")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("description", equalTo("Health Saving"))
               //294
               .body("id", equalTo("62d172e63b2f355e5ceafc9b"))
               .body("budgetedAmount", equalTo(400))
               .body("effectiveAmount", equalTo(500))
               .body("debitedAccount.name", equalTo("Health Savings"))
               .body("creditedAccount.name", equalTo("Checking"))
               .body("debitedAccount.underlyingAccount.accountType.value", equalTo(AccountType.CHECKING.getValue()));


        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               //294
               .param("id", "62d172e63b2f355e5ceafc9b")
               .get("/budget/transaction/")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("description", equalTo("Health Saving"))
               //294
               .body("id", equalTo("62d172e63b2f355e5ceafc9b"))
               .body("budgetedAmount", equalTo(400))
               .body("effectiveAmount", equalTo(500))
               .body("debitedAccount.name", equalTo("Health Savings"))
               .body("creditedAccount.name", equalTo("Checking"))
               .body("debitedAccount.underlyingAccount.accountType.value", equalTo(AccountType.CHECKING.getValue()));

    }


    @Test
    public void shouldGetTransaction() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               //294
               .param("id", "62d172e63b2f355e5ceafc9b")
               .get("/budget/transaction/")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("description", equalTo("Health Saving"))
               //294
               .body("id", equalTo("62d172e63b2f355e5ceafc9b"))
               .body("budgetedAmount", equalTo(400F))
               .body("effectiveAmount", equalTo(400F))
               .body("debitedAccount.name", equalTo("Health Savings"))
               .body("creditedAccount.name", equalTo("Checking"))
               .body("debitedAccount.underlyingAccount.accountType.value", equalTo(AccountType.CHECKING.getValue()));

    }

    @Test
    public void shouldDeleteTransaction() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               //294
               .param("transactionId", "62d172e63b2f355e5ceafc9b")
               .delete("/budget/transaction/delete")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK);

        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               //294
               .param("id", "62d172e63b2f355e5ceafc9b")
               .get("/budget/transaction/")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldDuplicateTransaction() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();

        RealAccountBoundaryDto underlyingAccount = RealAccountBoundaryDto
            .builder()
            //1
            .id("62d172d23b2f355e5ceafb5a")
            .name("Checking")
            .accountType(ValueEnumDto.builder()
                                     .value(AccountType.CHECKING.getValue())
                                     .build())
            .build();

        VirtualAccountBoundaryDto virtualAccountChecking = VirtualAccountBoundaryDto
            .builder()
            //4
            .id("62d172d93b2f355e5ceafb65")
            .name("Checking")
            .isDeleted(false).balance(BigDecimal.ZERO)
            .underlyingAccount(underlyingAccount)
            .build();

        VirtualAccountBoundaryDto virtualAccountHealth = VirtualAccountBoundaryDto
            .builder()
            //7
            .id("62d172d93b2f355e5ceafb68")
            .name("Health Savings")
            .isDeleted(false).balance(BigDecimal.ZERO)
            .underlyingAccount(underlyingAccount)
            .build();


        TransactionBoundaryDto input = TransactionBoundaryDto
            .builder()
            //294
            .id("62d172e63b2f355e5ceafc9b")
            .creditedAccount(virtualAccountChecking)
            .debitedAccount(virtualAccountHealth)
            .description("Health Saving")
            .date(LocalDate.of(2022, 2, 25))
            .paymentType(ValueEnumDto.builder()
                                     .value(PaymentType.DEPOSIT.getValue())
                                     .build())
            .paymentStatus(ValueEnumDto.builder()
                                       .value(PaymentStatus.PAID.getValue())
                                       .build())
            .indication(ValueEnumDto.builder()
                                    .value(TransactionIndication.EXPECTED.getValue())
                                    .build())
            .budgetedAmount(BigDecimal.valueOf(400))
            .effectiveAmount(BigDecimal.valueOf(400))
            .build();
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .body(asJsonString(input))
               .when()
               .post("/budget/transaction/dublicate")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void shouldGetAllTransactionsForMonth() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .param("date", "1654041600000")
               .get("/budget/transaction/list")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("$.size()", equalTo(168))
               .body("[0].description", equalTo("Going out"))
               .body("[0].budgetedAmount", equalTo(300F))
               .body("[0].effectiveAmount", equalTo(0F))
               .body("[0].debitedAccount.name", equalTo("Going Out"))
               .body("[0].creditedAccount.name", equalTo("Miles & More"))
               .body("[0].debitedAccount.underlyingAccount.accountType.value",
                   equalTo(AccountType.PREBUDGETED.getValue()))
               .body("[10].description", equalTo("RACE INN, ROGGWIL BE"))
               .body("[10].budgetedAmount", equalTo(0F))
               .body("[10].effectiveAmount", equalTo(5F))
               .body("[10].date", equalTo("2022-06-01"))
               .body("[10].debitedAccount.name", equalTo("Lunch Cyrill"))
               .body("[10].creditedAccount.name", equalTo("Miles & More"))
               .body("[10].debitedAccount.underlyingAccount.name", equalTo("Prebudget"))
               .body("[10].debitedAccount.underlyingAccount.accountType.value",
                   equalTo(AccountType.PREBUDGETED.getValue()));

    }

    @Test
    public void shouldGetAllTransactionsForMonthAndVirtualAccount() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .param("date", "1651363200000")
               //2
               .param("accountId", "62d172d93b2f355e5ceafb63")
               .get("/budget/transaction/listByMonthAndVirtualAccount")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("$.size()", equalTo(141))
               .body("[0].name", equalTo("Before"))
               .body("[0].balance", equalTo(0F))
               .body("[0].amount", equalTo(0))
               .body("[0].budgetedBalance", equalTo(0F))
               .body("[0].budgetedAmount", equalTo(0))
               .body("[0].id", equalTo("0"))

               .body("[131].name", equalTo("Creditcard Payment"))
               .body("[131].balance", equalTo(23.50F))
               .body("[131].amount", equalTo(2435.25F))
               .body("[131].budgetedBalance", equalTo(23.50F))
               .body("[131].budgetedAmount", equalTo(1923.5F))

               .body("[87].name", equalTo("JUSTEAT, ZURICH"))
               .body("[87].balance", equalTo(-2054.40F))
               .body("[87].amount", equalTo(-20.4F))
               .body("[87].budgetedBalance", equalTo(-2054.40F))
               .body("[87].budgetedAmount", equalTo(0F))
               //590

               .body("[139].name", equalTo("After"))
               .body("[139].balance", equalTo(0F))
               .body("[139].amount", equalTo(0))
               .body("[139].budgetedBalance", equalTo(0F))
               .body("[139].budgetedAmount", equalTo(0))
               .body("[139].id", equalTo("2147483646"))

               .body("[140].name", equalTo("In/Out"))
               .body("[140].balance", equalTo(4029.3F))
               .body("[140].amount", equalTo(4029.3F))
               .body("[140].budgetedBalance", equalTo(1923.5F))
               .body("[140].budgetedAmount", equalTo(1923.5F))
               .body("[140].id", equalTo("2147483647"));
    }

    @Test
    public void shouldGetAllTransactionsForMonthAndRealAccount() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .param("date", "1654041600000")
               //1
               .param("accountId", "62d172d23b2f355e5ceafb5a")
               .get("/budget/transaction/listByMonthAndRealAccount")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("$.size()", equalTo(32))
               .body("[0].name", equalTo("Before"))
               .body("[0].balance", equalTo(2337.54F))
               .body("[0].amount", equalTo(0))
               .body("[0].budgetedBalance", equalTo(2337.54F))
               .body("[0].budgetedAmount", equalTo(0))
               .body("[0].id", equalTo("0"))

               .body("[12].name", equalTo("Health insurance"))
               .body("[12].balance", equalTo(-2513.31F))
               .body("[12].amount", equalTo(-794.15F))
               .body("[12].budgetedBalance", equalTo(-1951.21F))
               .body("[12].budgetedAmount", equalTo(-794.15F))

               .body("[10].name", equalTo("Rent"))
               .body("[10].balance", equalTo(-1519.06F))
               .body("[10].amount", equalTo(-1356.0F))
               .body("[10].budgetedBalance", equalTo(-956.96F))
               .body("[10].budgetedAmount", equalTo(-1356.0F))

               .body("[31].name", equalTo("After"))
               .body("[31].balance", equalTo(2361.84F))
               .body("[31].amount", equalTo(0))
               .body("[31].budgetedBalance", equalTo(3387.99F))
               .body("[31].budgetedAmount", equalTo(0))
               .body("[31].id", equalTo("2147483646"));
    }

    @Test
    public void shouldGetPaymentTyps() {
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .get("/budget/transaction/type/list")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("$.size()", equalTo(3))
               .body("[0].name", equalTo("Deposit"))
               .body("[0].value", equalTo(1))
               .body("[1].name", equalTo("Standing Order"))
               .body("[1].value", equalTo(2))
               .body("[2].name", equalTo("e-bill"))
               .body("[2].value", equalTo(3));
    }

    @Test
    public void shouldGetIndicationTyps() {
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .get("/budget/transaction/indication/list")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("$.size()", equalTo(2))
               .body("[0].name", equalTo("E"))
               .body("[0].value", equalTo(1))
               .body("[1].name", equalTo("U"))
               .body("[1].value", equalTo(2));
    }

    @Test
    public void shouldGetStatusTyps() {
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .get("/budget/transaction/status/list")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("$.size()", equalTo(3))
               .body("[0].name", equalTo("open"))
               .body("[0].value", equalTo(1))
               .body("[1].name", equalTo("set up"))
               .body("[1].value", equalTo(2))
               .body("[2].name", equalTo("paid"))
               .body("[2].value", equalTo(3));
    }
}
