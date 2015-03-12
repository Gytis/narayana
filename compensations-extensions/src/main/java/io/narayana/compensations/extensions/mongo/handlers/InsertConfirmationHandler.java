package io.narayana.compensations.extensions.mongo.handlers;

//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import io.narayana.compensations.extensions.mongo.CollectionInfo;
//import org.bson.Document;
//import org.jboss.logging.Logger;
//import org.jboss.narayana.compensations.api.ConfirmationHandler;
//
//import javax.inject.Inject;
//import java.util.Set;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
//public class InsertConfirmationHandler implements ConfirmationHandler {
//
//    private static final Logger LOGGER = Logger.getLogger(InsertConfirmationHandler.class);
//
//    @Inject
//    private InsertHandlerData insertHandlerData;
//
//    @Override
//    public void confirm() {
//        final Set<CollectionInfo> collectionInfoSet = insertHandlerData.getCollectionInfoSet();
//
//        if (collectionInfoSet.isEmpty()) {
//            return;
//        }
//
//        final MongoClient mongoClient = new MongoClient();
//
//        for (final CollectionInfo collectionInfo : insertHandlerData.getCollectionInfoSet()) {
//            final MongoDatabase database = mongoClient.getDatabase(collectionInfo.getDatabaseName());
//            final MongoCollection<Document> collection = database.getCollection(collectionInfo.getCollectionName());
//
//            confirmCollection(collection, insertHandlerData.getTransactionId());
//            insertHandlerData.removeCollectionInfo(collectionInfo);
//        }
//
//        mongoClient.close();
//    }
//
//    private void confirmCollection(final MongoCollection<Document> collection, final String transactionId) {
//        final Document filter = new Document("txinfo.transactionId", transactionId);
//
//        for (final Document document : collection.find(filter)) {
//            confirmDocument(collection, document);
//            // TODO catch and log any heuristic exceptions
//        }
//    }
//
//    private void confirmDocument(final MongoCollection<Document> collection, final Document document) {
//        final Document updateQuery = new Document("$unset", new Document("txinfo", null));
//        final Document updateFiler = new Document("_id", document.get("_id"));
//
//        LOGGER.debug("Confirming transaction in document: " + document.get("_id"));
//
//        collection.updateOne(updateFiler, updateQuery);
//    }
//
//}
