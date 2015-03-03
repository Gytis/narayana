package io.narayana.compensations.extensions.mongo.integration;

import com.mongodb.client.MongoCollection;
import io.narayana.compensations.extensions.mongo.CollectionConfiguration;
import org.bson.Document;
import org.jboss.narayana.compensations.api.Compensatable;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class DatabaseManager {

    @Inject
    @CollectionConfiguration(databaseName = "testdb", collectionName = "test")
    private MongoCollection<Document> collection;

    @Compensatable
    public void insert(final String key, final String value) {
        collection.insertOne(new Document(key, value));
    }

    public Iterator<Document> getEntries() {
        return collection.find().iterator();
    }

    public void clearEntries() {
        collection.dropCollection();
    }

}
