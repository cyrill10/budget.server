package ch.bader.budget.server;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DataUtils {

    public static MysqlDataSource getMySQLDataSource() throws IOException {

        Properties props = new Properties();
        FileInputStream fis;
        MysqlDataSource ds;

        fis = new FileInputStream("src/main/resources/application.properties");
        props.load(fis);

        ds = new MysqlConnectionPoolDataSource();
        ds.setURL(props.getProperty("spring.datasource.url").replace("mysql-db", "localhost"));
        ds.setUser(props.getProperty("spring.datasource.username"));
        ds.setPassword(props.getProperty("spring.datasource.password"));

        return ds;
    }

    public static String getMogoDataSourceString() throws IOException {
        Properties props = new Properties();
        FileInputStream fis;

        fis = new FileInputStream("src/main/resources/application.properties");
        props.load(fis);

        return "mongodb://" +
            props.getProperty("spring.data.mongodb.username") +
            ":" +
            props.getProperty("spring.data.mongodb.password") +
            "@localhost";
    }


}
