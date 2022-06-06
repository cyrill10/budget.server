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
}
