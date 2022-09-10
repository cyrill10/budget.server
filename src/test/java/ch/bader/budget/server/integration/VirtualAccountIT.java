package ch.bader.budget.server.integration;

import ch.bader.budget.server.boundary.dto.RealAccountBoundaryDto;
import ch.bader.budget.server.boundary.dto.ValueEnumDto;
import ch.bader.budget.server.boundary.dto.VirtualAccountBoundaryDto;
import ch.bader.budget.server.type.AccountType;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static ch.bader.budget.server.TestUtils.asJsonString;
import static ch.bader.budget.server.TestUtils.getAuthHeader;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VirtualAccountIT extends AbstractIT {

    @Test
    public void shouldLoadDb() {
        //arrange
        assertDoesNotThrow(this::populateDatabaseFull);
    }

    @Test
    public void shouldAddAccount() {
        RealAccountBoundaryDto underlyingAccount = RealAccountBoundaryDto
            .builder()
            //4
            .id("62a50222ce7b3719fa1aac5f")
            .name("Credit Cards")
            .accountType(ValueEnumDto.builder()
                                     .value(AccountType.CREDIT.getValue())
                                     .build())
            .build();

        VirtualAccountBoundaryDto input = VirtualAccountBoundaryDto.builder()
                                                                   .name("TestVirtual")
                                                                   .underlyingAccount(underlyingAccount)
                                                                   .build();

        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .body(asJsonString(input))
               .when()
               .post("/budget/virtualAccount/add/")
               .then()
               .statusCode(HttpStatus.SC_CREATED)
               .body("name", equalTo("TestVirtual"))
               .body("id", isA(String.class))
               .body("balance", equalTo(0))
               .body("isDeleted", equalTo(false))
               //4
               .body("underlyingAccount.id", equalTo("62a50222ce7b3719fa1aac5f"));
    }

    @Test
    public void shouldUpdateAccount() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();
        //act
        RealAccountBoundaryDto underlyingAccount = RealAccountBoundaryDto
            .builder()
            //5
            .id("62d172d33b2f355e5ceafb5e")
            .name("Prebudgeted")
            .accountType(ValueEnumDto.builder()
                                     .value(AccountType.PREBUDGETED.getValue())
                                     .build())
            .build();

        VirtualAccountBoundaryDto input = VirtualAccountBoundaryDto
            .builder()
            //8
            .id("62d172da3b2f355e5ceafb69")
            .name("TestVirtual")
            .underlyingAccount(underlyingAccount)
            .balance(BigDecimal.ZERO)
            .isDeleted(false)
            .build();

        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .body(asJsonString(input))
               .when()
               .put("/budget/virtualAccount/update")
               .then()
               .statusCode(HttpStatus.SC_OK)
               .body("name", equalTo("TestVirtual"))
               //8
               .body("id", equalTo("62d172da3b2f355e5ceafb69"))
               .body("balance", equalTo(0))
               .body("isDeleted", equalTo(false))
               //5
               .body("underlyingAccount.id", equalTo("62d172d33b2f355e5ceafb5e"));


        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               //8
               .param("id", "62d172da3b2f355e5ceafb69")
               .get("/budget/virtualAccount/")
               .then()
               .statusCode(HttpStatus.SC_OK)
               .body("name", equalTo("TestVirtual"))
               //8
               .body("id", equalTo("62d172da3b2f355e5ceafb69"))
               .body("balance", equalTo(0))
               .body("isDeleted", equalTo(false))
               //5
               .body("underlyingAccount.id", equalTo("62d172d33b2f355e5ceafb5e"));
    }


    @Test
    public void shouldGetAccount() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               //6
               .param("id", "62d172d93b2f355e5ceafb67")
               .get("/budget/virtualAccount/")
               .then()
               .statusCode(HttpStatus.SC_OK)
               .body("name", equalTo("General Expenses"))
               //6
               .body("id", equalTo("62d172d93b2f355e5ceafb67"))
               //1
               .body("underlyingAccount.id", equalTo("62d172d23b2f355e5ceafb5a"));

    }

    @Test
    public void shouldGetAllAccounts() throws IOException, URISyntaxException {
        //arrange
        populateDatabaseFull();
        //act
        given().headers(getAuthHeader()).contentType(ContentType.JSON)
               .when()
               .get("/budget/virtualAccount/list")
               .then()
               .statusCode(HttpStatus.SC_OK)
               .body("$.size()", equalTo(34))
               //3
               .body("[0].id", equalTo("62d172d93b2f355e5ceafb64"))
               .body("[0].name", equalTo("Bonviva"))
               //4
               .body("[0].underlyingAccount.id", equalTo("62d172d33b2f355e5ceafb5d"));
    }

}
