package io.narayana.compensations.extensions.mongo.handlers;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.jboss.logging.Logger;
import org.jboss.narayana.compensations.api.ConfirmationHandler;
import org.jboss.narayana.compensations.impl.BAControllerFactory;

import java.util.Iterator;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class InsertConfirmationHandler implements ConfirmationHandler {

    private static final Logger LOGGER = Logger.getLogger(InsertConfirmationHandler.class);

    private final MongoClient mongoClient;

    public InsertConfirmationHandler() {
        mongoClient = new MongoClient();
    }

    @Override
    public void confirm() {
        String transactionId = null;
        try {
            transactionId = BAControllerFactory.getInstance().getCurrentTransaction().toString();
        } catch (Exception e) {
            LOGGER.error("Failed to get transaction id", e);
        }

        if (transactionId == null) {
            return;
        }

        for (final String databaseName: mongoClient.getDatabaseNames()) {
            LOGGER.debug("Scanning <" + databaseName + "> database for transactions");
            confirmDatabase(mongoClient.getDatabase(databaseName), transactionId);
        }
    }

    public void confirmDatabase(final MongoDatabase database, final String transactionId) {
        for (final String collectionName : database.listCollectionNames()) {
            if (collectionName.equals(collectionName)) {
                LOGGER.debug("Scanning <" + collectionName + "> collection for transactions");
                confirmCollection(database.getCollection(collectionName, Document.class), transactionId);
            }
        }
    }

    public void confirmCollection(final MongoCollection<Document> collection, final String transactionId) {
        final Document filter = new Document("txinfo.transactionId", transactionId);
        final Iterator<Document> iterator = collection.find(filter).iterator();

        for (final Document documentToUpdate : collection.find(filter)) {
            final Document updateQuery = new Document("$unset", new Document("txinfo", null));
            final Document updateFiler = new Document("_id", documentToUpdate.get("_id"));

            LOGGER.debug("Confirming transaction in document: " + documentToUpdate.get("_id"));

            collection.updateOne(updateFiler, updateQuery);
        }
    }

}
