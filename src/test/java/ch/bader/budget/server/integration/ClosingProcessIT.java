package ch.bader.budget.server.integration;

import ch.bader.budget.server.TestUtils;
import ch.bader.budget.server.boundary.dto.SaveScannedTransactionBoundaryDto;
import ch.bader.budget.server.type.AccountType;
import ch.bader.budget.server.type.ClosingProcessStatus;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static ch.bader.budget.server.DataUtils.getMySQLDataSource;
import static ch.bader.budget.server.TestUtils.asJsonString;
import static ch.bader.budget.server.TestUtils.getAuthHeader;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClosingProcessIT extends AbstractIT {

    @BeforeAll
    void initDatabase() throws IOException {
        dataSource = getMySQLDataSource();
    }

    @Test
    public void shouldLoadDb() throws IOException {
        //arrange
        populateDatabaseFull();
    }

    @Test
    public void whenProcessExists_shouldGetClosingProcess() throws IOException {
        //arrange
        populateDatabaseFull();

        //act + assert
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .param("year", 2022)
               .param("month", 4)
               .get("/budget/closingProcess")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("year", equalTo(2022))
               .body("month", equalTo(4))
               .body("uploadStatus.value", equalTo(ClosingProcessStatus.DONE.getValue()))
               .body("manualEntryStatus.value", equalTo(ClosingProcessStatus.NEW.getValue()));
    }

    @Test
    public void whenProcessDoesNotExists_shouldCreateAndGetClosingProcess() throws IOException {
        //arrange
        populateDatabaseFull();

        //act + assert
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .param("year", 2022)
               .param("month", 1)
               .get("/budget/closingProcess")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("year", equalTo(2022))
               .body("month", equalTo(1))
               .body("uploadStatus.value", equalTo(ClosingProcessStatus.NEW.getValue()))
               .body("manualEntryStatus.value", equalTo(ClosingProcessStatus.NEW.getValue()));
    }

    @Test
    public void whenCloseFileUpload_shouldCloseUploadStatus() throws IOException {
        //arrange
        populateDatabaseFull();

        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .param("year", 2022)
               .param("month", 1)
               .get("/budget/closingProcess")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("year", equalTo(2022))
               .body("month", equalTo(1))
               .body("uploadStatus.value", equalTo(ClosingProcessStatus.NEW.getValue()))
               .body("manualEntryStatus.value", equalTo(ClosingProcessStatus.NEW.getValue()));

        //act + assert
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .queryParam("year", 2022)
               .queryParam("month", 1)
               .post("/budget/closingProcess/closeFileUpload")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("year", equalTo(2022))
               .body("month", equalTo(1))
               .body("uploadStatus.value", equalTo(ClosingProcessStatus.DONE.getValue()))
               .body("manualEntryStatus.value", equalTo(ClosingProcessStatus.NEW.getValue()));


        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .param("year", 2022)
               .param("month", 1)
               .get("/budget/closingProcess")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("year", equalTo(2022))
               .body("month", equalTo(1))
               .body("uploadStatus.value", equalTo(ClosingProcessStatus.DONE.getValue()))
               .body("manualEntryStatus.value", equalTo(ClosingProcessStatus.NEW.getValue()));
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ScannendTransaction {

        @Test
        @Order(1)
        void uploadFile() throws IOException {
            //arrange
            populateDatabaseFull();

            File file = TestUtils.loadFile("creditCard/exampleTransactions.csv");

            //act + assert
            given().headers(getAuthHeader()).contentType(ContentType.MULTIPART)
                   .when()
                   .queryParam("year", 2021)
                   .queryParam("month", 11)
                   .multiPart(file)
                   .post("/budget/closingProcess")
                   .then()
                   .log().all()
                   .statusCode(HttpStatus.SC_OK)
                   .body("$.size()", equalTo(127))
                   .body("[0].date", equalTo("2022-04-20"))
                   .body("[0].description", equalTo("LAEDERACH CHOCOLAT. AG, BERN"))
                   .body("[0].amount", equalTo(9.9F))
                   .body("[0].cardType", equalTo("MasterCard"))
                   .body("[0].transactionCreated", equalTo(false))
                   .body("[126].date", equalTo("2022-05-20"))
                   .body("[126].description", equalTo("TRAVEL COMFORT PLUS"))
                   .body("[126].amount", equalTo(13.5F))
                   .body("[126].cardType", equalTo("AMEX"))
                   .body("[126].transactionCreated", equalTo(false));
        }

        @Test
        @Order(2)
        void saveScannedTransactions_withoutThroughAccount() {

            //act + assert
            JsonPath jsonPath = given().headers(getAuthHeader()).contentType(ContentType.JSON)
                                       .when()
                                       .queryParam("year", 2021)
                                       .queryParam("month", 11)
                                       .get("/budget/closingProcess/transactions")
                                       .then()
                                       .log().all()
                                       .statusCode(HttpStatus.SC_OK)
                                       .body("$.size()", equalTo(127))
                                       .body("[0].date", equalTo("2022-04-20"))
                                       .body("[0].description", equalTo("LAEDERACH CHOCOLAT. AG, BERN"))
                                       .body("[0].amount", equalTo(9.9F))
                                       .body("[0].cardType", equalTo("MasterCard"))
                                       .body("[0].transactionCreated", equalTo(false))
                                       .body("[126].date", equalTo("2022-05-20"))
                                       .body("[126].description", equalTo("TRAVEL COMFORT PLUS"))
                                       .body("[126].amount", equalTo(13.5F))
                                       .body("[126].cardType", equalTo("AMEX"))
                                       .body("[126].transactionCreated", equalTo(false))
                                       .body("[15].transactionCreated", equalTo(false))
                                       .body("[22].transactionCreated", equalTo(false))
                                       .body("[43].transactionCreated", equalTo(false))
                                       .body("[67].transactionCreated", equalTo(false))
                                       .body("[81].transactionCreated", equalTo(false))
                                       .body("[91].transactionCreated", equalTo(false))
                                       .body("[115].transactionCreated", equalTo(false))
                                       .extract().jsonPath();

            String transactionId1 = jsonPath.getString("[15].id");
            String transactionId2 = jsonPath.getString("[22].id");
            String transactionId3 = jsonPath.getString("[43].id");
            String transactionId4 = jsonPath.getString("[67].id");
            String transactionId5 = jsonPath.getString("[81].id");
            String transactionId6 = jsonPath.getString("[91].id");
            String transactionId7 = jsonPath.getString("[115].id");
            List<String> transactionIds = List.of(transactionId1,
                transactionId2,
                transactionId3,
                transactionId4,
                transactionId5,
                transactionId6,
                transactionId7);

            SaveScannedTransactionBoundaryDto body = SaveScannedTransactionBoundaryDto
                .builder()
                .transactionIds(transactionIds)
                .creditedAccountId("2")
                .debitedAccountId("10")
//                .creditedAccountId("62ace92f84611622284424cd")
//                .debitedAccountId("62ace92f84611622284424d5")
                .build();


            given().headers(getAuthHeader()).contentType(ContentType.JSON)
                   .body(asJsonString(body))
                   .when()
                   .post("/budget/closingProcess/transactions")
                   .then()
                   .log().all()
                   .statusCode(HttpStatus.SC_OK);


            given().headers(getAuthHeader()).contentType(ContentType.JSON)
                   .when()
                   .queryParam("year", 2021)
                   .queryParam("month", 11)
                   .get("/budget/closingProcess/transactions")
                   .then()
                   .log().all()
                   .statusCode(HttpStatus.SC_OK)
                   .body("$.size()", equalTo(127))
                   .body("[0].date", equalTo("2022-04-20"))
                   .body("[0].description", equalTo("LAEDERACH CHOCOLAT. AG, BERN"))
                   .body("[0].amount", equalTo(9.9F))
                   .body("[0].cardType", equalTo("MasterCard"))
                   .body("[0].transactionCreated", equalTo(false))
                   .body("[126].date", equalTo("2022-05-20"))
                   .body("[126].description", equalTo("TRAVEL COMFORT PLUS"))
                   .body("[126].amount", equalTo(13.5F))
                   .body("[126].cardType", equalTo("AMEX"))
                   .body("[126].transactionCreated", equalTo(true))
                   .body("[15].transactionCreated", equalTo(true))
                   .body("[22].transactionCreated", equalTo(true))
                   .body("[43].transactionCreated", equalTo(true))
                   .body("[67].transactionCreated", equalTo(true))
                   .body("[81].transactionCreated", equalTo(true))
                   .body("[91].transactionCreated", equalTo(true))
                   .body("[115].transactionCreated", equalTo(true));

            given().headers(getAuthHeader()).contentType(ContentType.JSON)
                   .when()
                   .param("date", "1638316800000")
                   .get("/budget/transaction/list")
                   .then()
                   .log().all()
                   .statusCode(HttpStatus.SC_OK)
                   .body("$.size()", equalTo(7))
                   .body("[0].description", equalTo("Internet"))
                   .body("[0].budgetedAmount", equalTo(45F))
                   .body("[0].effectiveAmount", equalTo(0F))
                   .body("[0].debitedAccount.name", equalTo("Internet & Phone"))
                   .body("[0].creditedAccount.name", equalTo("Checking"))
                   .body("[0].debitedAccount.underlyingAccount.accountType.value",
                       equalTo(AccountType.ALIEN.getValue()))
                   .body("[10].description", equalTo("Salary"))
                   .body("[10].budgetedAmount", equalTo(7363.6F))
                   .body("[10].effectiveAmount", equalTo(0F))
                   .body("[10].date", equalTo("2022-06-25"))
                   .body("[10].debitedAccount.name", equalTo("Checking"))
                   .body("[10].creditedAccount.name", equalTo("Salary Cyrill"))
                   .body("[10].debitedAccount.underlyingAccount.name", equalTo("Checking"))
                   .body("[10].debitedAccount.underlyingAccount.accountType.value",
                       equalTo(AccountType.CHECKING.getValue()));

        }
    }
}
