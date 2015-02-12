package io.narayana.compensations.extensions.firsttry.mongo.handlers;

import io.narayana.compensations.extensions.firsttry.CompensationData;
import io.narayana.compensations.extensions.firsttry.OperationHandler;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonTimestamp;
import org.jboss.logging.Logger;
import org.jboss.narayana.compensations.api.ConfirmationHandler;

import javax.inject.Inject;
import java.util.Date;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class InsertHandler implements OperationHandler<BsonDocument>, ConfirmationHandler {

    private static final Logger LOGGER = Logger.getLogger(InsertHandler.class);

    @Inject
    private CompensationData<BsonDocument> compensationData;

    @Override
    public BsonDocument execute(final BsonDocument document) {
        document.put("txinfo", getTransactionData());

        LOGGER.info("InsertHandler.execute attached transaction data to the document: " + document);

        return document;
    }

    @Override
    public void confirm() {
        LOGGER.info("InsertHandler.confirm");
        // Remove transaction data
    }

    private BsonDocument getTransactionData() {
        final int timestamp = (int) new Date().getTime() / 1000;

        final BsonDocument document = new BsonDocument();
        document.put("timestamp", new BsonTimestamp(timestamp, 0));
        document.put("txid", new BsonString("kuku"));

        return document;
    }
}
