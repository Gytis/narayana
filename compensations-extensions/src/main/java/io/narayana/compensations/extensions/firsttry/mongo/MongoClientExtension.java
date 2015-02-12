package io.narayana.compensations.extensions.firsttry.mongo;

import com.mongodb.MongoClient;
import io.narayana.compensations.extensions.firsttry.Extension;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class MongoClientExtension implements Extension<MongoClient> {

    @Override
    public MongoClient getInstance() {
        return new CompensatableMongoClient();
    }

    @Override
    public MongoClient getInstance(MongoClient mongoClient) {
        return new CompensatableMongoClient(mongoClient);
    }

}
