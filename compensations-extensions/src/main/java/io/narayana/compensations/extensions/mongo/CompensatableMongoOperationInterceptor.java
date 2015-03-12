package io.narayana.compensations.extensions.mongo;

import io.narayana.compensations.extensions.mongo.handlers.InsertHandlerData;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.jboss.logging.Logger;
import org.jboss.narayana.compensations.impl.BAControler;
import org.jboss.narayana.compensations.impl.BAControllerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@CompensatableMongoOperation
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 198)
public class CompensatableMongoOperationInterceptor {

    private static final Logger LOGGER = Logger.getLogger(CompensatableMongoOperationInterceptor.class);

    private static final String TRANSACTION_DATA_COLUMN_NAME = TransactionData.class.getSimpleName();

    @Inject
    private InsertHandlerData insertHandlerData;

    @AroundInvoke
    public Object intercept(final InvocationContext ic) throws Exception {
        final String transactionId = getTransactionId();
        if (transactionId == null) {
            return ic.proceed();
        }

        final CompensatableMongoCollection collection = getCollection(ic);
        if (collection == null) {
            return ic.proceed();
        }

        switch (ic.getMethod().getName()) {
            case "insertOne": handleInsertOne(ic, transactionId, collection);
        }

        return ic.proceed();
    }

    private void handleInsertOne(final InvocationContext ic, final String transactionId,
            final CompensatableMongoCollection collection) {

        final BsonDocument newState = encodeBsonDocument(ic.getParameters()[0], collection);
        final BsonDocument updatedNewState = attachTransactionData(transactionId, null, newState);
        final Object document = decodeBsonDocument(updatedNewState, collection);
        final Object[] parameters = new Object[] {
                document
        };

        insertHandlerData.setTransactionId(transactionId);
        insertHandlerData.addCollectionInfo(new CollectionInfo(collection.getDatabaseName(), collection.getCollectionName()));

        ic.setParameters(parameters);
    }

    private String getTransactionId() {
        final BAControler baController = BAControllerFactory.getInstance();

        if (!baController.isBARunning()) {
            return null;
        }

        try {
            return baController.getCurrentTransaction().toString();
        } catch (Exception e) {
            LOGGER.warn("Failed to get currently running transaction", e);
            return null;
        }
    }

    private CompensatableMongoCollection getCollection(final InvocationContext ic) {
        final Object collection = ic.getTarget();

        if (collection instanceof CompensatableMongoCollection) {
            return (CompensatableMongoCollection) collection;
        }

        return null;
    }

    private BsonDocument attachTransactionData(final String transactionId, final BsonDocument originalState,
            final BsonDocument newState) {

        // Only doing this for originalState, because newState cannot be null
        String originalStateString = null;
        if (originalState != null) {
            originalStateString = originalState.toString();
        }

        final TransactionData transactionData = new TransactionData(transactionId, originalStateString,
                newState.toString());
        newState.append(TRANSACTION_DATA_COLUMN_NAME, transactionData.toBsonDocument());

        return newState;
    }

    private BsonDocument encodeBsonDocument(final Object object, final CompensatableMongoCollection collection) {
        if (object == null) {
            return null;
        }

        final CodecRegistry codecRegistry = collection.getCodecRegistry();

        return BsonDocumentWrapper.asBsonDocument(object, codecRegistry);
    }
    
    private Object decodeBsonDocument(final BsonDocument document, final CompensatableMongoCollection collection) {
        final CodecRegistry codecRegistry = collection.getCodecRegistry();
        final Class documentClass = collection.getDocumentClass();
        final BsonDocumentReader bsonDocumentReader = new BsonDocumentReader(document);
        final DecoderContext decoderContext = DecoderContext.builder().build();

        return codecRegistry.get(documentClass).decode(bsonDocumentReader, decoderContext);
    }

}
