package ch.bader.budget.server.integration;

import ch.bader.budget.server.type.ClosingProcessStatus;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static ch.bader.budget.server.DataUtils.getMySQLDataSource;
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

}
