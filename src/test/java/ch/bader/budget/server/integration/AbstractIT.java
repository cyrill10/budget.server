package ch.bader.budget.server.integration;

import ch.bader.budget.server.DataUtils;
import ch.bader.budget.server.TestUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mysql.cj.jdbc.MysqlDataSource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.bson.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractIT {

    protected MysqlDataSource dataSource;

    protected void populateDatabaseFull() throws IOException {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("/sql/fullDatabase.sql"));
        populator.execute(dataSource);

        populateMongoDbs();
    }

    private void populateMongoDbs() throws IOException {
        populateMongoDb("virtualAccount");
        populateMongoDb("realAccount");
        populateMongoDb("transaction");
        populateMongoDb("closingProcess");
    }

    private void populateMongoDb(String collectionName) throws IOException {
        String virtualAccountsString = TestUtils.loadFile("dump/budget/" + collectionName + ".txt");

        try (MongoClient mongoClient = MongoClients.create(DataUtils.getMogoDataSourceString())) {
            MongoDatabase db = mongoClient.getDatabase("budget");
            MongoCollection<Document> collection = db.getCollection(collectionName);
            collection.deleteMany(new Document());
            List<Document> jsonList = new ArrayList<>();
            JSONArray array = JSONArray.fromObject(virtualAccountsString);
            for (
                Object object : array) {
                JSONObject jsonStr = (JSONObject) JSONSerializer.toJSON(object);
                Document jsnObject = Document.parse(jsonStr.toString());
                jsonList.add(jsnObject);

            }
            collection.insertMany(jsonList);
        }
    }
}
