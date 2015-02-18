package io.narayana.compensations.extensions.mongo.integration;

import com.mongodb.client.MongoCollection;
import io.narayana.compensations.extensions.mongo.CollectionConfiguration;
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

    @Inject
    @CollectionConfiguration(databaseName = "testdb", collectionName = "test")
    private MongoCollection<Document> collection;

    @TxCompensate(InsertCompensationHandler.class)
    @TxConfirm(InsertConfirmationHandler.class)
    public void insert(final String key, final String value) {
        collection.insertOne(new Document(key, value));
    }

    public Iterator<Document> getAll() {
        return collection.find().iterator();
    }

    public void clear() {
        collection.dropCollection();
    }

}
