package ch.bader.budget.server.integration;

import ch.bader.budget.server.DataUtils;
import ch.bader.budget.server.TestUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.bson.Document;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractIT {

    protected void populateDatabaseFull() throws IOException, URISyntaxException {
        populateMongoDbs();
    }

    private void populateMongoDbs() throws IOException, URISyntaxException {
        populateMongoDb("virtualAccount");
        populateMongoDb("realAccount");
        populateMongoDb("transaction");
        populateMongoDb("closingProcess");
        populateMongoDb("scannedTransaction");
    }

    private void populateMongoDb(String collectionName) throws IOException, URISyntaxException {
        String virtualAccountsString = TestUtils.loadFileAsString("dump/budget/" + collectionName + ".txt");

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
