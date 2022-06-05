package ch.bader.budget.server.integration;

import org.springframework.http.HttpHeaders;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractIntegrationTest {

    private static final MySQLContainer mySQLContainer;

    protected HttpHeaders getAuthHeader() {
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth("user", "password");
        return header;
    }

    static {
        mySQLContainer = (MySQLContainer) (new MySQLContainer("mysql:8.0")
                .withUsername("testcontainers")
                .withPassword("Testcontain3rs!")
                .withReuse(true));
        mySQLContainer
                .withInitScript("sql/fullDatabase.sql");
        mySQLContainer.start();
    }

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
    }

}
