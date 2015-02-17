package io.narayana.compensations.extensions.mongo.integration;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.jboss.narayana.compensations.api.TxCompensate;
import org.jboss.narayana.compensations.api.TxConfirm;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class DatabaseManager {

    private static final String DB_NAME = "testdb";

    private static final String COLLECTION_NAME = "test";

    private final MongoDatabase database;

    @Inject
    public DatabaseManager(final MongoClient client) {
        database = client.getDatabase(DB_NAME);
    }

    @TxCompensate(InsertCompensationHandler.class)
    @TxConfirm(InsertConfirmationHandler.class)
    public void insertDocument(final String key, final String value) {
        database.getCollection(COLLECTION_NAME).insertOne(new Document(key, value));
    }

    @TxCompensate(InsertCompensationHandler.class)
    @TxConfirm(InsertConfirmationHandler.class)
    public void insertBsonDocument(final String key, final String value) {
        database.getCollection(COLLECTION_NAME, BsonDocument.class).insertOne(new BsonDocument(key, new BsonString(value)));
    }

    public Iterator<Document> getAll() {
        return database.getCollection(COLLECTION_NAME).find().iterator();
    }

    public void clear() {
        database.getCollection(COLLECTION_NAME).dropCollection();
    }

}
