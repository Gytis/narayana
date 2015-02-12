package io.narayana.compensations.extensions.firsttry.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public final class CompensatableMongoClient extends MongoClient {

    public CompensatableMongoClient(final MongoClient mongoClient) {
        super(mongoClient.getServerAddressList(), mongoClient.getCredentialsList(), mongoClient.getMongoClientOptions());
        mongoClient.close();
    }

    public CompensatableMongoClient() {
        super();
    }

    public CompensatableMongoClient(String host) {
        super(host);
    }

    public CompensatableMongoClient(String host, MongoClientOptions options) {
        super(host, options);
    }

    public CompensatableMongoClient(String host, int port) {
        super(host, port);
    }

    public CompensatableMongoClient(ServerAddress addr) {
        super(addr);
    }

    public CompensatableMongoClient(ServerAddress addr, List<MongoCredential> credentialsList) {
        super(addr, credentialsList);
    }

    public CompensatableMongoClient(ServerAddress addr, MongoClientOptions options) {
        super(addr, options);
    }

    public CompensatableMongoClient(ServerAddress addr, List<MongoCredential> credentialsList, MongoClientOptions options) {
        super(addr, credentialsList, options);
    }

    public CompensatableMongoClient(List<ServerAddress> seeds) {
        super(seeds);
    }

    public CompensatableMongoClient(List<ServerAddress> seeds, List<MongoCredential> credentialsList) {
        super(seeds, credentialsList);
    }

    public CompensatableMongoClient(List<ServerAddress> seeds, MongoClientOptions options) {
        super(seeds, options);
    }

    public CompensatableMongoClient(List<ServerAddress> seeds, List<MongoCredential> credentialsList, MongoClientOptions options) {
        super(seeds, credentialsList, options);
    }

    public CompensatableMongoClient(MongoClientURI uri) {
        super(uri);
    }

    /**
     * Return decorated MongoDatabase.
     *
     * @param databaseName
     * @return MongoDatabase
     */
    public MongoDatabase getDatabase(final String databaseName) {
        final MongoDatabase mongoDatabase = super.getDatabase(databaseName);

        return new CompensatableMongoDatabase(mongoDatabase);
    }

}
