package io.narayana.compensations.extensions.mongo;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.CreateIndexOptions;
import com.mongodb.client.model.FindOneAndDeleteOptions;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.RenameCollectionOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.narayana.compensations.extensions.mongo.handlers.InsertConfirmationHandler;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.jboss.narayana.compensations.api.TxConfirm;

import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class CompensatableMongoCollection<TDocument> implements MongoCollection<TDocument> {

    private MongoCollection<TDocument> delegate;

    private String databaseName;

    private String collectionName;

    public CompensatableMongoCollection() {
        // Should only be invoked by CDI
    }

    public void setDelegate(final MongoCollection<TDocument> delegate) {
        this.delegate = delegate;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(final String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    @CompensatableMongoOperation
    @TxConfirm(InsertConfirmationHandler.class)
    public void insertOne(final TDocument document) {
        delegate.insertOne(document);
    }


    /*
     * The rest of the delegated methods which are not decorated.
     */

    @Override
    public MongoNamespace getNamespace() {
        return delegate.getNamespace();
    }

    @Override
    public Class<TDocument> getDocumentClass() {
        return delegate.getDocumentClass();
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

    /**
     * TODO
     */
    @Override
    public <NewTDocument> MongoCollection<NewTDocument> withDocumentClass(final Class<NewTDocument> clazz) {
        return delegate.withDocumentClass(clazz);
    }

    /**
     * TODO
     */
    @Override
    public MongoCollection<TDocument> withCodecRegistry(final CodecRegistry codecRegistry) {
        return delegate.withCodecRegistry(codecRegistry);
    }

    /**
     * TODO
     */
    @Override
    public MongoCollection<TDocument> withReadPreference(final ReadPreference readPreference) {
        return delegate.withReadPreference(readPreference);
    }

    /**
     * TODO
     */
    @Override
    public MongoCollection<TDocument> withWriteConcern(final WriteConcern writeConcern) {
        return delegate.withWriteConcern(writeConcern);
    }

    @Override
    public long count() {
        return delegate.count();
    }

    @Override
    public long count(final Bson filter) {
        return delegate.count(filter);
    }

    @Override
    public long count(final Bson filter, final CountOptions options) {
        return delegate.count(filter, options);
    }

    @Override
    public <TResult> DistinctIterable<TResult> distinct(final String fieldName, final Class<TResult> resultClass) {
        return delegate.distinct(fieldName, resultClass);
    }

    @Override
    public FindIterable<TDocument> find() {
        return delegate.find();
    }

    @Override
    public <TResult> FindIterable<TResult> find(final Class<TResult> resultClass) {
        return delegate.find(resultClass);
    }

    @Override
    public FindIterable<TDocument> find(final Bson filter) {
        return delegate.find(filter);
    }

    @Override
    public <TResult> FindIterable<TResult> find(final Bson filter, final Class<TResult> resultClass) {
        return delegate.find(filter, resultClass);
    }

    @Override
    public AggregateIterable<TDocument> aggregate(final List<? extends Bson> pipeline) {
        return delegate.aggregate(pipeline);
    }

    @Override
    public <TResult> AggregateIterable<TResult> aggregate(final List<? extends Bson> pipeline, final Class<TResult> resultClass) {
        return delegate.aggregate(pipeline, resultClass);
    }

    @Override
    public MapReduceIterable<TDocument> mapReduce(final String mapFunction, final String reduceFunction) {
        return delegate.mapReduce(mapFunction, reduceFunction);
    }

    @Override
    public <TResult> MapReduceIterable<TResult> mapReduce(final String mapFunction, final String reduceFunction,
                                                          final Class<TResult> resultClass) {
        return delegate.mapReduce(mapFunction, reduceFunction, resultClass);
    }

    /**
     * TODO
     */
    @Override
    public BulkWriteResult bulkWrite(final List<? extends WriteModel<? extends TDocument>> requests) {
        return delegate.bulkWrite(requests);
    }

    /**
     * TODO
     */
    @Override
    @SuppressWarnings("unchecked")
    public BulkWriteResult bulkWrite(final List<? extends WriteModel<? extends TDocument>> requests, final BulkWriteOptions options) {
        return delegate.bulkWrite(requests, options);
    }

    /**
     * TODO
     */
    @Override
    public void insertMany(final List<? extends TDocument> documents) {
        delegate.insertMany(documents);
    }

    /**
     * TODO
     */
    @Override
    public void insertMany(final List<? extends TDocument> documents, final InsertManyOptions options) {
        delegate.insertMany(documents, options);
    }

    /**
     * TODO
     */
    @Override
    public DeleteResult deleteOne(final Bson filter) {
        return delegate.deleteOne(filter);
    }

    /**
     * TODO
     */
    @Override
    public DeleteResult deleteMany(final Bson filter) {
        return delegate.deleteOne(filter);
    }

    /**
     * TODO
     */
    @Override
    public UpdateResult replaceOne(final Bson filter, final TDocument replacement) {
        return delegate.replaceOne(filter, replacement);
    }

    /**
     * TODO
     */
    @Override
    public UpdateResult replaceOne(final Bson filter, final TDocument replacement, final UpdateOptions updateOptions) {
        return delegate.replaceOne(filter, replacement, updateOptions);
    }

    /**
     * TODO
     */
    @Override
    public UpdateResult updateOne(final Bson filter, final Bson update) {
        return delegate.updateOne(filter, update);
    }

    /**
     * TODO
     */
    @Override
    public UpdateResult updateOne(final Bson filter, final Bson update, final UpdateOptions updateOptions) {
        return delegate.updateOne(filter, update, updateOptions);
    }

    /**
     * TODO
     */
    @Override
    public UpdateResult updateMany(final Bson filter, final Bson update) {
        return delegate.updateMany(filter, update);
    }

    /**
     * TODO
     */
    @Override
    public UpdateResult updateMany(final Bson filter, final Bson update, final UpdateOptions updateOptions) {
        return delegate.updateMany(filter, update, updateOptions);
    }

    /**
     * TODO
     */
    @Override
    public TDocument findOneAndDelete(final Bson filter) {
        return delegate.findOneAndDelete(filter);
    }

    /**
     * TODO
     */
    @Override
    public TDocument findOneAndDelete(final Bson filter, final FindOneAndDeleteOptions options) {
        return delegate.findOneAndDelete(filter, options);
    }

    /**
     * TODO
     */
    @Override
    public TDocument findOneAndReplace(final Bson filter, final TDocument replacement) {
        return delegate.findOneAndReplace(filter, replacement);
    }

    /**
     * TODO
     */
    @Override
    public TDocument findOneAndReplace(final Bson filter, final TDocument replacement, final FindOneAndReplaceOptions options) {
        return delegate.findOneAndReplace(filter, replacement, options);
    }

    /**
     * TODO
     */
    @Override
    public TDocument findOneAndUpdate(final Bson filter, final Bson update) {
        return delegate.findOneAndUpdate(filter, update);
    }

    /**
     * TODO
     */
    @Override
    public TDocument findOneAndUpdate(final Bson filter, final Bson update, final FindOneAndUpdateOptions options) {
        return delegate.findOneAndUpdate(filter, update, options);
    }

    @Override
    public void dropCollection() {
        delegate.dropCollection();
    }

    @Override
    public void createIndex(final Bson key) {
        delegate.createIndex(key);
    }

    @Override
    public void createIndex(final Bson key, final CreateIndexOptions createIndexOptions) {
        delegate.createIndex(key, createIndexOptions);
    }

    @Override
    public ListIndexesIterable<Document> listIndexes() {
        return delegate.listIndexes();
    }

    @Override
    public <TResult> ListIndexesIterable<TResult> listIndexes(final Class<TResult> resultClass) {
        return delegate.listIndexes(resultClass);
    }

    @Override
    public void dropIndex(final String indexName) {
        delegate.dropIndex(indexName);
    }

    @Override
    public void dropIndexes() {
        delegate.dropIndexes();
    }

    @Override
    public void renameCollection(final MongoNamespace newCollectionNamespace) {
        delegate.renameCollection(newCollectionNamespace);
    }

    @Override
    public void renameCollection(final MongoNamespace newCollectionNamespace, final RenameCollectionOptions renameCollectionOptions) {
        delegate.renameCollection(newCollectionNamespace, renameCollectionOptions);
    }

}
