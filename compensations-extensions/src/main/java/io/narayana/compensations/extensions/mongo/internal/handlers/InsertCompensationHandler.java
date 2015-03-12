package io.narayana.compensations.extensions.mongo.internal.handlers;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.narayana.compensations.extensions.mongo.internal.CollectionInfo;
import io.narayana.compensations.extensions.mongo.internal.TransactionData;
import org.bson.Document;
import org.jboss.logging.Logger;
import org.jboss.narayana.compensations.api.CompensationHandler;

import javax.inject.Inject;
import java.util.Set;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class InsertCompensationHandler implements CompensationHandler {

    private static final Logger LOGGER = Logger.getLogger(InsertConfirmationHandler.class);

    private static final String TRANSACTION_DATA_COLUMN_NAME = TransactionData.class.getSimpleName();

    @Inject
    private InsertHandlerData insertHandlerData;

    @Override
    public void compensate() {
        final String transactionId = insertHandlerData.getTransactionId();
        if (transactionId == null) {
            return;
        }

        final Set<CollectionInfo> collectionInfoSet = insertHandlerData.getCollectionInfoSet();
        if (collectionInfoSet.isEmpty()) {
            return;
        }

        // Not using injection, because transaction is not needed here
        final MongoClient mongoClient = new MongoClient();

        for (final CollectionInfo collectionInfo : collectionInfoSet) {
            final MongoDatabase database = mongoClient.getDatabase(collectionInfo.getDatabaseName());
            final MongoCollection<Document> collection = database.getCollection(collectionInfo.getCollectionName());

            compensateCollection(collection, transactionId);
            insertHandlerData.removeCollectionInfo(collectionInfo);
        }

        mongoClient.close();
    }

    private void compensateCollection(final MongoCollection<Document> collection, final String transactionId) {
        final Document filter = new Document(TRANSACTION_DATA_COLUMN_NAME + ".transactionId", transactionId);

        for (final Document document : collection.find(filter)) {
            compensateDocument(collection, document);
            // TODO catch and log any heuristic exceptions
        }
    }

    private void compensateDocument(final MongoCollection<Document> collection, final Document document) {
        final Object transactionDataObject = document.get(TRANSACTION_DATA_COLUMN_NAME);
        if (!(transactionDataObject instanceof Document)) {
            return;
        }

        final Document transactionData = (Document) transactionDataObject;

        if (isCompensationValid(document, transactionData)) {
            final Document deleteFilter = new Document("_id", document.get("_id"));
            collection.deleteOne(deleteFilter);
        } else {
            LOGGER.warn("Heuristic compensation outcome: document was changed. Document: " + document);
        }
    }

    /**
     * TODO what about the window between the check and update?
     *
     * @return
     */
    private boolean isCompensationValid(final Document document, final Document transactionData) {
        final String newStateString = transactionData.getString("newState");
        final Document newState = Document.valueOf(newStateString);

        final Document copy = new Document(document);
        copy.remove(TRANSACTION_DATA_COLUMN_NAME);
        copy.remove("_id");

        return newState.equals(copy);
    }

}
