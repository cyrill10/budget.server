package ch.bader.budget.server.integration;

import ch.bader.budget.server.boundary.time.MonthGenerator;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ch.bader.budget.server.TestUtils.getAuthHeader;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DateIT {


    @Test
    void shouldReturnMonths() {
        // Given

        LocalDate firstDay = MonthGenerator.STARTDATE;
        LocalDate expectedLastDate = LocalDate.now().plusYears(1).withDayOfMonth(1);

        long diff = ChronoUnit.MONTHS.between(
            firstDay,
            expectedLastDate);
        // When & Then
        JsonPath response = given().headers(getAuthHeader()).contentType(ContentType.JSON)
                                   .when()
                                   .get("/budget/date/month/list")
                                   .then()
                                   .statusCode(HttpStatus.SC_OK)
                                   .body("[0]", equalTo(firstDay.toString()))
                                   .extract().jsonPath();

        List<String> list = response.get("$");
        assertEquals(diff, list.size());
    }
}
