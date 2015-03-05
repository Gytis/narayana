package io.narayana.compensations.extensions.mongo;

import io.narayana.compensations.extensions.mongo.handlers.InsertHandlerData;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.DecoderContext;
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

    @Inject
    private InsertHandlerData insertHandlerData;

    @AroundInvoke
    public Object intercept(final InvocationContext ic) throws Exception {
        if (!(ic.getTarget() instanceof CompensatableMongoCollection)) {
            return ic.proceed();
        }

        final CompensatableMongoCollection collection = (CompensatableMongoCollection) ic.getTarget();

        switch (ic.getMethod().getName()) {
            case "insertOne": handleInsertOne(collection, ic);
        }

        return ic.proceed();
    }

    private void handleInsertOne(final CompensatableMongoCollection collection, final InvocationContext ic) {
        final BAControler baController = BAControllerFactory.getInstance();

        if (!baController.isBARunning()) {
            return;
        }

        final String transactionId;
        try {
            transactionId = baController.getCurrentTransaction().toString();
        } catch (Exception e) {
            LOGGER.warn("Failed to get currently running transaction", e);
            return;
        }

        final Object document = ic.getParameters()[0];
        if (document == null) {
            return;
        }

        final BsonDocument bsonDocument = BsonDocumentWrapper.asBsonDocument(document, collection.getCodecRegistry());
        final TransactionData transactionData = new TransactionData(transactionId, null, bsonDocument.toString());
        bsonDocument.put("txinfo", transactionData.toBsonDocument());

        final Object updatedDocument = collection.getCodecRegistry().get(collection.getDocumentClass()).decode(new BsonDocumentReader(bsonDocument), DecoderContext.builder().build());
        ic.setParameters(new Object[] { updatedDocument });

        insertHandlerData.setTransactionId(transactionId);
        insertHandlerData.addCollectionInfo(new CollectionInfo(collection.getDatabaseName(), collection.getCollectionName()));
    }

}
