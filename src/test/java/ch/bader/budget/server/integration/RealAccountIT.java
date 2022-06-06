package ch.bader.budget.server.integration;

import ch.bader.budget.server.boundary.dto.RealAccountBoundaryDto;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.type.AccountType;
import com.mysql.cj.jdbc.MysqlDataSource;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.io.IOException;

import static ch.bader.budget.server.DataUtils.getMySQLDataSource;
import static ch.bader.budget.server.TestUtils.asJsonString;
import static ch.bader.budget.server.TestUtils.getAuthHeader;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RealAccountIT {

    MysqlDataSource dataSource;

    @BeforeAll
    void initDatabase() throws IOException {
        dataSource = getMySQLDataSource();

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("/sql/fullDatabase.sql"));
        populator.execute(dataSource);
    }


    @Test
    public void shouldAddAccount() {
        RealAccountBoundaryDto input = RealAccountBoundaryDto.builder()
                                                             .name("TestAccount")
                                                             .accountType(ValueEnumDto.builder()
                                                                                      .value(AccountType.CHECKING.getValue())
                                                                                      .build())
                                                             .build();

        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .body(asJsonString(input))
               .when()
               .post("/budget/realAccount/add/")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_CREATED)
               .body("name", equalTo("TestAccount"))
               .body("id", matchesRegex("\\d+"))
               .body("accountType.value", equalTo(AccountType.CHECKING.getValue()));
    }

    @Test
    public void shouldUpdateAccount() {
        RealAccountBoundaryDto input = RealAccountBoundaryDto.builder()
                                                             .id("5")
                                                             .name("TestAccount2")
                                                             .accountType(ValueEnumDto.builder()
                                                                                      .value(AccountType.CREDIT.getValue())
                                                                                      .build())
                                                             .build();

        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .body(asJsonString(input))
               .when()
               .put("/budget/realAccount/update")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("name", equalTo("TestAccount2"))
               .body("id", equalTo("5"))
               .body("accountType.value", equalTo(AccountType.CREDIT.getValue()));

        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .param("id", 5)
               .get("/budget/realAccount/")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("name", equalTo("TestAccount2"))
               .body("id", matchesRegex("\\d+"))
               .body("accountType.value", equalTo(AccountType.CREDIT.getValue()));
    }


    @Test
    public void shouldGetAccount() {

        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .param("id", 1)
               .get("/budget/realAccount/")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("name", equalTo("Checking"))
               .body("id", matchesRegex("\\d+"))
               .body("accountType.value", equalTo(AccountType.CHECKING.getValue()));
    }

    @Test
    public void shouldGetAllAccounts() {

        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .get("/budget/realAccount/list")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("$.size()", equalTo(9));
    }

    @Test
    public void shouldGetAllAccountTyps() {

        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .get("/budget/realAccount/type/list")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("$.size()", equalTo(5))
               .body("[0].name", equalTo("Checking"))
               .body("[0].value", equalTo(1))
               .body("[1].name", equalTo("Saving"))
               .body("[1].value", equalTo(2))
               .body("[2].name", equalTo("Credit"))
               .body("[2].value", equalTo(3))
               .body("[3].name", equalTo("Alien"))
               .body("[3].value", equalTo(4))
               .body("[4].name", equalTo("Prebudgeted"))
               .body("[4].value", equalTo(5));
    }
}
