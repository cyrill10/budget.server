package ch.bader.budget.server.integration;

import ch.bader.budget.server.boundary.dto.RealAccountBoundaryDto;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.type.AccountType;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static ch.bader.budget.server.DataUtils.getMySQLDataSource;
import static ch.bader.budget.server.TestUtils.asJsonString;
import static ch.bader.budget.server.TestUtils.getAuthHeader;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RealAccountIT extends AbstractIT {

    @BeforeAll
    void initDatabase() throws IOException {
        dataSource = getMySQLDataSource();
    }

    @Test
    public void shouldLoadDb() {
        //arrange
        populateDatabaseFull();
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
               .body("id", isA(String.class))
               .body("accountType.value", equalTo(AccountType.CHECKING.getValue()));
    }

    @Test
    public void shouldUpdateAccount() {
        //arrange
        populateDatabaseFull();
        //act
        RealAccountBoundaryDto input = RealAccountBoundaryDto.builder()
                                                             .id("62a2560999508e3db411c854")
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
               .body("id", equalTo("62a2560999508e3db411c854"))
               .body("accountType.value", equalTo(AccountType.CREDIT.getValue()));

        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .param("id", "62a2560999508e3db411c854")
               .get("/budget/realAccount/")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("name", equalTo("TestAccount2"))
               .body("id", equalTo("62a2560999508e3db411c854"))
               .body("accountType.value", equalTo(AccountType.CREDIT.getValue()));
    }


    @Test
    public void shouldGetAccount() {
        //arrange
        populateDatabaseFull();
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .param("id", "62a2560999508e3db411c850")
               .get("/budget/realAccount/")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("name", equalTo("Checking"))
               .body("id", equalTo("62a2560999508e3db411c850"))
               .body("accountType.value", equalTo(AccountType.CHECKING.getValue()));
    }

    @Test
    public void shouldGetAllAccounts() {
        //arrange
        populateDatabaseFull();
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .get("/budget/realAccount/list")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("$.size()", equalTo(8));
    }

    @Test
    public void shouldGetAllAccountTyps() {
        //act
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
