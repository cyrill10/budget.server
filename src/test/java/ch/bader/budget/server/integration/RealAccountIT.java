package ch.bader.budget.server.integration;

import ch.bader.budget.server.boundary.dto.RealAccountBoundaryDto;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.type.AccountType;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.net.URISyntaxException;

import static ch.bader.budget.server.TestUtils.asJsonString;
import static ch.bader.budget.server.TestUtils.getAuthHeader;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RealAccountIT extends AbstractIT {

    @Test
    public void shouldLoadDb() throws IOException, URISyntaxException {
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
    public void shouldUpdateAccount() throws IOException, URISyntaxException {
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
    public void shouldGetAccount() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               //1
               .param("id", "62d172d23b2f355e5ceafb5a")
               .get("/budget/realAccount/")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("name", equalTo("Checking"))
               //1
               .body("id", equalTo("62d172d23b2f355e5ceafb5a"))
               .body("accountType.value", equalTo(AccountType.CHECKING.getValue()));
    }

    @Test
    public void shouldGetAllAccounts() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .get("/budget/realAccount/list")
               .then()
               .log().all()
               .statusCode(HttpStatus.SC_OK)
               .body("$.size()", equalTo(8))
               .body("[0].realAccount.name", equalTo("Checking"))
               .body("[0].virtualAccounts.size()", equalTo(5))
               .body("[1].virtualAccounts[1].name", equalTo("Libera Lyka"))
               .body("[7].realAccount.name", equalTo("Prebudget"))
               .body("[7].virtualAccounts.size()", equalTo(5))
               .body("[7].virtualAccounts[0].name", equalTo("Going Out"))
               .body("[7].virtualAccounts[0].underlyingAccount.accountType.value", equalTo(5));
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
