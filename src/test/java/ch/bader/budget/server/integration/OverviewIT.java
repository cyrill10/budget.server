package ch.bader.budget.server.integration;

import ch.bader.budget.server.TestUtils;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

import java.io.IOException;
import java.net.URISyntaxException;

import static ch.bader.budget.server.TestUtils.getAuthHeader;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OverviewIT extends AbstractIT {

    @Test
    void shouldLoadDb() {
        //arrange
        assertDoesNotThrow(this::populateDatabaseFull);
    }

    @Test
    void shouldGetOverview() throws IOException, URISyntaxException, JSONException {
        //arrange
        populateDatabaseFull();

        String mills2022May1 = "1651363200000";
        JSONArray expectedJson = new JSONArray(JsonPath
                .from(TestUtils.loadFileAsString("json/overview.json"))
                .getList(""));

        //act + assert
        JSONArray response =
                new JSONArray(given().headers(getAuthHeader()).contentType(ContentType.JSON)
                        .when()
                        .param("dateLong", mills2022May1)
                        .get("/budget/overview/list/")
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().jsonPath().getList(""));

        JSONCompareResult compareResult = JSONCompare.compareJSON(expectedJson,
                response,
                JSONCompareMode.LENIENT);
        compareResult.getFieldFailures()
                .forEach(fieldComparisonFailure -> System.out.println("Failed " +
                        fieldComparisonFailure.getField() + " Expected: " +
                        fieldComparisonFailure.getExpected() + " Actual: " +
                        fieldComparisonFailure.getActual()));
        assertTrue(compareResult.passed());
    }
}
