package io.narayana.compensations.extensions.firsttry.mongo.integration;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.narayana.compensations.extensions.firsttry.mongo.MongoClientExtension;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class StandaloneCompensationsTest {

    private static final MongoClientExtension MONGO_CLIENT_EXTENSION = new MongoClientExtension();

    private MongoDatabase db;

    @Before
    public void before() {
        db = MONGO_CLIENT_EXTENSION.getInstance().getDatabase("testdb");

        final MongoCollection<Document> collection = db.getCollection("testcol");
        if (collection != null) {
            collection.dropCollection();
        }
    }

    @Test
    public void testInsertOne() {
        db.createCollection("testcol");
        final MongoCollection<Document> collection = db.getCollection("testcol");
        collection.insertOne(new Document("testkey", "testvalue"));

        final Iterator<Document> result = collection.find().iterator();
        Assert.assertTrue(result.hasNext());

        final Document document = result.next();
        Assert.assertEquals("testvalue", document.get("testkey"));
        Assert.assertFalse(result.hasNext());
    }

}
