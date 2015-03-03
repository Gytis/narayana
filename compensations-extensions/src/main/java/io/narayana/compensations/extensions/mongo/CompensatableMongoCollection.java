package io.narayana.compensations.extensions.mongo;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindFluent;
import com.mongodb.client.ListIndexesFluent;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.AggregateOptions;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.CreateIndexOptions;
import com.mongodb.client.model.DistinctOptions;
import com.mongodb.client.model.FindOneAndDeleteOptions;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.MapReduceOptions;
import com.mongodb.client.model.RenameCollectionOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.narayana.compensations.extensions.mongo.handlers.InsertConfirmationHandler;
import io.narayana.compensations.extensions.mongo.handlers.InsertHandlerData;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.jboss.narayana.compensations.api.TxConfirm;
import org.jboss.narayana.compensations.impl.BAControler;
import org.jboss.narayana.compensations.impl.BAControllerFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class CompensatableMongoCollection implements MongoCollection<Document> {

    @Inject
    private InsertHandlerData insertHandlerData;

    private MongoCollection<Document> delegate;

    private String databaseName;

    private String collectionName;

    public CompensatableMongoCollection() {
        // Should only be invoked by CDI
    }

    public void setDelegate(final MongoCollection<Document> delegate) {
        this.delegate = delegate;
    }

    public void setDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
    }

    public void setCollectionName(final String collectionName) {
        this.collectionName = collectionName;
    }

    // Decorated methods

    @Override
    @TxConfirm(InsertConfirmationHandler.class)
    public void insertOne(Document document) {
        // TODO move to the better place
        final BAControler baControler = BAControllerFactory.getInstance();

        if (baControler.isBARunning()) {
            try {
                final Object currentTransaction = baControler.getCurrentTransaction();
                final TransactionData transactionData = new TransactionData(currentTransaction.toString(), null,
                        document.toString());
                document.put("txinfo", transactionData.toDocument());

                insertHandlerData.setTransactionId(currentTransaction.toString());
                insertHandlerData.addCollectionInfo(new CollectionInfo(databaseName, collectionName));
            } catch (final Exception e) {
                throw new RuntimeException("Failed to get currently running transaction", e);
            }
        }

        delegate.insertOne(document);
    }

    @Override
    public <C> MongoCollection<C> withDefaultClass(Class<C> clazz) {
        // TODO
        return delegate.withDefaultClass(clazz);
    }

    @Override
    public MongoCollection<Document> withCodecRegistry(CodecRegistry codecRegistry) {
        // TODO
        return delegate.withCodecRegistry(codecRegistry);
    }

    @Override
    public MongoCollection<Document> withReadPreference(ReadPreference readPreference) {
        // TODO
        return delegate.withReadPreference(readPreference);
    }

    @Override
    public MongoCollection<Document> withWriteConcern(WriteConcern writeConcern) {
        // TODO
        return delegate.withWriteConcern(writeConcern);
    }

    @Override
    public BulkWriteResult bulkWrite(List<? extends WriteModel<? extends Document>> requests) {
        // TODO
        return delegate.bulkWrite(requests);
    }

    @Override
    public BulkWriteResult bulkWrite(List<? extends WriteModel<? extends Document>> requests, BulkWriteOptions options) {
        // TODO
        return delegate.bulkWrite(requests, options);
    }

    @Override
    public void insertMany(List<? extends Document> documents) {
        // TODO
        delegate.insertMany(documents);
    }

    @Override
    public void insertMany(List<? extends Document> documents, InsertManyOptions options) {
        // TODO
        delegate.insertMany(documents, options);
    }

    @Override
    public DeleteResult deleteOne(Object filter) {
        // TODO
        return delegate.deleteOne(filter);
    }

    @Override
    public DeleteResult deleteMany(Object filter) {
        // TODO
        return delegate.deleteMany(filter);
    }

    @Override
    public UpdateResult replaceOne(Object filter, Document replacement) {
        // TODO
        return delegate.replaceOne(filter, replacement);
    }

    @Override
    public UpdateResult replaceOne(Object filter, Document replacement, UpdateOptions updateOptions) {
        // TODO
        return delegate.replaceOne(filter, replacement, updateOptions);
    }

    @Override
    public UpdateResult updateOne(Object filter, Object update) {
        // TODO
        return delegate.updateOne(filter, update);
    }

    @Override
    public UpdateResult updateOne(Object filter, Object update, UpdateOptions updateOptions) {
        // TODO
        return delegate.updateOne(filter, update, updateOptions);
    }

    @Override
    public UpdateResult updateMany(Object filter, Object update) {
        // TODO
        return delegate.updateMany(filter, update);
    }

    @Override
    public UpdateResult updateMany(Object filter, Object update, UpdateOptions updateOptions) {
        // TODO
        return delegate.updateMany(filter, update, updateOptions);
    }

    @Override
    public Document findOneAndDelete(Object filter) {
        // TODO
        return delegate.findOneAndDelete(filter);
    }

    @Override
    public Document findOneAndDelete(Object filter, FindOneAndDeleteOptions options) {
        // TODO
        return delegate.findOneAndDelete(filter, options);
    }

    @Override
    public Document findOneAndReplace(Object filter, Document replacement) {
        // TODO
        return delegate.findOneAndReplace(filter, replacement);
    }

    @Override
    public Document findOneAndReplace(Object filter, Document replacement, FindOneAndReplaceOptions options) {
        // TODO
        return delegate.findOneAndReplace(filter, replacement, options);
    }

    @Override
    public Document findOneAndUpdate(Object filter, Object update) {
        // TODO
        return delegate.findOneAndUpdate(filter, update);
    }

    @Override
    public Document findOneAndUpdate(Object filter, Object update, FindOneAndUpdateOptions options) {
        // TODO
        return delegate.findOneAndUpdate(filter, update, options);
    }


    // Delegated methods

    @Override
    public MongoNamespace getNamespace() {
        return delegate.getNamespace();
    }

    @Override
    public Class<Document> getDefaultClass() {
        return delegate.getDefaultClass();
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
    public long count() {
        return delegate.count();
    }

    @Override
    public long count(Object filter) {
        return delegate.count(filter);
    }

    @Override
    public long count(Object filter, CountOptions options) {
        return delegate.count(filter, options);
    }

    @Override
    public List<Object> distinct(String fieldName, Object filter) {
        return delegate.distinct(fieldName, filter);
    }

    @Override
    public List<Object> distinct(String fieldName, Object filter, DistinctOptions distinctOptions) {
        return delegate.distinct(fieldName, filter, distinctOptions);
    }

    @Override
    public FindFluent<Document> find() {
        return delegate.find();
    }

    @Override
    public <C> FindFluent<C> find(Class<C> clazz) {
        return delegate.find(clazz);
    }

    @Override
    public FindFluent<Document> find(Object filter) {
        return delegate.find(filter);
    }

    @Override
    public <C> FindFluent<C> find(Object filter, Class<C> clazz) {
        return delegate.find(filter, clazz);
    }

    @Override
    public MongoIterable<Document> aggregate(List<?> pipeline) {
        return delegate.aggregate(pipeline);
    }

    @Override
    public <C> MongoIterable<C> aggregate(List<?> pipeline, Class<C> clazz) {
        return delegate.aggregate(pipeline, clazz);
    }

    @Override
    public MongoIterable<Document> aggregate(List<?> pipeline, AggregateOptions options) {
        return delegate.aggregate(pipeline, options);
    }

    @Override
    public <C> MongoIterable<C> aggregate(List<?> pipeline, AggregateOptions options, Class<C> clazz) {
        return delegate.aggregate(pipeline, options, clazz);
    }

    @Override
    public MongoIterable<Document> mapReduce(String mapFunction, String reduceFunction) {
        return delegate.mapReduce(mapFunction, reduceFunction);
    }

    @Override
    public MongoIterable<Document> mapReduce(String mapFunction, String reduceFunction, MapReduceOptions options) {
        return delegate.mapReduce(mapFunction, reduceFunction, options);
    }

    @Override
    public <C> MongoIterable<C> mapReduce(String mapFunction, String reduceFunction, Class<C> clazz) {
        return delegate.mapReduce(mapFunction, reduceFunction, clazz);
    }

    @Override
    public <C> MongoIterable<C> mapReduce(String mapFunction, String reduceFunction, MapReduceOptions options, Class<C> clazz) {
        return delegate.mapReduce(mapFunction, reduceFunction, options, clazz);
    }

    @Override
    public void dropCollection() {
        delegate.dropCollection();
    }

    @Override
    public void createIndex(Object key) {
        delegate.createIndex(key);
    }

    @Override
    public void createIndex(Object key, CreateIndexOptions createIndexOptions) {
        delegate.createIndex(key, createIndexOptions);
    }

    @Override
    public ListIndexesFluent<Document> listIndexes() {
        return delegate.listIndexes();
    }

    @Override
    public <C> ListIndexesFluent<C> listIndexes(Class<C> clazz) {
        return delegate.listIndexes(clazz);
    }

    @Override
    public void dropIndex(String indexName) {
        delegate.dropIndex(indexName);
    }

    @Override
    public void dropIndexes() {
        delegate.dropIndexes();
    }

    @Override
    public void renameCollection(MongoNamespace newCollectionNamespace) {
        delegate.renameCollection(newCollectionNamespace);
    }

    @Override
    public void renameCollection(MongoNamespace newCollectionNamespace, RenameCollectionOptions renameCollectionOptions) {
        delegate.renameCollection(newCollectionNamespace, renameCollectionOptions);
    }

}
