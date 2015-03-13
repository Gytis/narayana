package io.narayana.compensations.extensions.mongo.integration;

import com.mongodb.client.MongoCollection;
import io.narayana.compensations.extensions.mongo.CollectionConfiguration;
import org.bson.Document;
import org.jboss.narayana.compensations.api.Compensatable;
import org.jboss.narayana.compensations.api.CompensationTransactionType;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class DatabaseManager {

    @Inject
    @CollectionConfiguration(databaseName = "testdb", collectionName = "test")
    private MongoCollection<Document> collection;

    @Compensatable
    public void insert(final DatabaseEntry entry) {
        collection.insertOne(new Document(entry.getKey(), entry.getValue()));
    }

    @Compensatable(CompensationTransactionType.NOT_SUPPORTED)
    public void updateWithoutTransaction(final DatabaseEntry originalEntry, final DatabaseEntry newEntry) {
        final Document updateFiler = new Document(originalEntry.getKey(), originalEntry.getValue());
        final Document updateQuery = new Document("$set", new Document(newEntry.getKey(), newEntry.getValue()));

        collection.updateOne(updateFiler, updateQuery);
    }

    public Iterable<Document> getEntries() {
        return collection.find();
    }

    public Iterable<Document> getEntries(final DatabaseEntry entry) {
        return collection.find(new Document(entry.getKey(), entry.getValue()));
    }

    public long getCount() {
        return collection.count();
    }

    public void clearEntries() {
        collection.dropCollection();
    }

}
