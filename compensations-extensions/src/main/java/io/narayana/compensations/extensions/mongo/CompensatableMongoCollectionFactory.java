package io.narayana.compensations.extensions.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class CompensatableMongoCollectionFactory {

    private MongoClient mongoClient;

    @Produces
    @CollectionConfiguration(collectionName = "", databaseName = "")
    public MongoCollection<Document> getMongoCollection(InjectionPoint injectionPoint,
            @New CompensatableMongoCollection compensatableMongoCollection) {

        if (mongoClient == null) {
            mongoClient = new MongoClient();
        }

        final CollectionConfiguration collectionConfiguration =
                injectionPoint.getAnnotated().getAnnotation(CollectionConfiguration.class);

        final MongoCollection<Document> delegate = mongoClient.getDatabase(collectionConfiguration.databaseName())
                .getCollection(collectionConfiguration.collectionName());

        compensatableMongoCollection.setDelegate(delegate);
        compensatableMongoCollection.setDatabaseName(collectionConfiguration.databaseName());
        compensatableMongoCollection.setCollectionName(collectionConfiguration.collectionName());

        return compensatableMongoCollection;
    }

}
