package io.narayana.compensations.extensions.mongo.integration;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import io.narayana.compensations.extensions.mongo.MongoCompensatable;
import org.jboss.javaee.mongodb.Mongo;
import org.jboss.javaee.mongodb.MongoClientDefinition;
import org.jboss.narayana.compensations.api.Compensatable;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@MongoClientDefinition(name = "Localhost Mongo Def", url = "mongodb://localhost")
public class DatabaseManager {

    @Inject
    @Mongo(db = "testdb", collection = "test")
    @MongoCompensatable
    private DBCollection collection;

    @Compensatable
    public void insert(final String key, final String value) {
        collection.insert(new BasicDBObject(key, value));
    }

    public Iterator<DBObject> getEntries() {
        return collection.find().iterator();
    }

    public void clearEntries() {
        collection.drop();
    }

}
