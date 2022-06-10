package ch.bader.budget.server.integration;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public abstract class AbstractIT {

    protected MysqlDataSource dataSource;

    protected void populateDatabaseFull() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("/sql/fullDatabase.sql"));
        populator.execute(dataSource);
    }
}
