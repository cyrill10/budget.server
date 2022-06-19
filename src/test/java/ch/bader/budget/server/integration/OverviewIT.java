package ch.bader.budget.server.integration;

import ch.bader.budget.server.TestUtils;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.net.URISyntaxException;

import static ch.bader.budget.server.TestUtils.getAuthHeader;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OverviewIT extends AbstractIT {

    @Test
    public void shouldLoadDb() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();
    }

    @Test
    public void shouldGetOverview() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();

        String mills2022May1 = "1651363200000";
        JsonPath expectedJson = JsonPath.from(TestUtils.loadFileAsString("json/overview.json"));

        //act + assert
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .param("dateLong", mills2022May1)
               .get("/budget/overview/list/")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("", equalTo(expectedJson.getList("")));
    }
}
