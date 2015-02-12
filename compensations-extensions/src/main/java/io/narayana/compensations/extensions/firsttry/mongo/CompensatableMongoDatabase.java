package io.narayana.compensations.extensions.firsttry.mongo;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.ListCollectionsFluent;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.CreateCollectionOptions;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public final class CompensatableMongoDatabase implements MongoDatabase {

    private final MongoDatabase delegate;

    public CompensatableMongoDatabase(final MongoDatabase delegate) {
        this.delegate = delegate;
    }

    // Decorated methods

    @Override
    public MongoDatabase withCodecRegistry(CodecRegistry codecRegistry) {
        final MongoDatabase mongoDatabase = delegate.withCodecRegistry(codecRegistry);

        return new CompensatableMongoDatabase(mongoDatabase);
    }

    @Override
    public MongoDatabase withReadPreference(ReadPreference readPreference) {
        final MongoDatabase mongoDatabase = delegate.withReadPreference(readPreference);

        return new CompensatableMongoDatabase(mongoDatabase);
    }

    @Override
    public MongoDatabase withWriteConcern(WriteConcern writeConcern) {
        final MongoDatabase mongoDatabase = delegate.withWriteConcern(writeConcern);

        return new CompensatableMongoDatabase(mongoDatabase);
    }

    @Override
    public MongoCollection<Document> getCollection(String collectionName) {
        final MongoCollection<Document> mongoCollection = delegate.getCollection(collectionName);

        return new CompensatableMongoCollection<>(mongoCollection);
    }

    @Override
    public <T> MongoCollection<T> getCollection(String collectionName, Class<T> clazz) {
        final MongoCollection<T> mongoCollection = delegate.getCollection(collectionName, clazz);

        return new CompensatableMongoCollection<>(mongoCollection);
    }

    @Override
    public Document executeCommand(Object command) {
        // TODO
        return delegate.executeCommand(command);
    }

    @Override
    public Document executeCommand(Object command, ReadPreference readPreference) {
        // TODO
        return delegate.executeCommand(command, readPreference);
    }

    @Override
    public <T> T executeCommand(Object command, Class<T> clazz) {
        // TODO
        return delegate.executeCommand(command, clazz);
    }

    @Override
    public <T> T executeCommand(Object command, ReadPreference readPreference, Class<T> clazz) {
        // TODO
        return delegate.executeCommand(command, readPreference, clazz);
    }

    // Delegated methods

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public CodecRegistry getCodecRegistry() {
        return delegate.getCodecRegistry();
    }

    @Override
    public ReadPreference getReadPreference() {
        return delegate.getReadPreference();
    }

    @Override
    public WriteConcern getWriteConcern() {
        return delegate.getWriteConcern();
    }

    @Override
    public void dropDatabase() {
        delegate.dropDatabase();
    }

    @Override
    public MongoIterable<String> listCollectionNames() {
        return delegate.listCollectionNames();
    }

    @Override
    public ListCollectionsFluent<Document> listCollections() {
        return delegate.listCollections();
    }

    @Override
    public <C> ListCollectionsFluent<C> listCollections(Class<C> clazz) {
        return delegate.listCollections(clazz);
    }

    @Override
    public void createCollection(String collectionName) {
        delegate.createCollection(collectionName);
    }

    @Override
    public void createCollection(String collectionName, CreateCollectionOptions createCollectionOptions) {
        delegate.createCollection(collectionName, createCollectionOptions);
    }
}
