package io.narayana.compensations.extensions.mongo.unit;

import io.narayana.compensations.extensions.mongo.TransactionData;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TransactionDataTest {

    @Test
    public void testToStringWithNulls() {
        final TransactionData transactionData = new TransactionData(null, null, null, null);

        Assert.assertEquals("<TransactionData: transactionId=null, originalState=null, newState=null, timestamp=null>",
                transactionData.toString());
    }

    @Test
    public void testToString() {
        final TransactionData transactionData = getTransactionData(true, true, true, true);
        final String expectedString = String.format("<%s: transactionId=%s, originalState=%s, newState=%s, timestamp=%s>",
                TransactionData.class.getSimpleName(), transactionData.getTransactionId(),
                transactionData.getOriginalState(), transactionData.getNewState(),
                transactionData.getTimestamp().getTime());

        Assert.assertEquals(expectedString, transactionData.toString());
    }

    @Test
    public void testToStringWithoutOriginalState() {
        final TransactionData transactionData = getTransactionData(true, false, true, true);
        final String expectedString = String.format("<%s: transactionId=%s, originalState=null, newState=%s, timestamp=%s>",
                TransactionData.class.getSimpleName(), transactionData.getTransactionId(),
                transactionData.getNewState(), transactionData.getTimestamp().getTime());

        Assert.assertEquals(expectedString, transactionData.toString());
    }

    @Test
    public void testToStringWithoutNewState() {
        final TransactionData transactionData = getTransactionData(true, true, false, true);
        final String expectedString = String.format("<%s: transactionId=%s, originalState=%s, newState=null, timestamp=%s>",
                TransactionData.class.getSimpleName(), transactionData.getTransactionId(),
                transactionData.getOriginalState(), transactionData.getTimestamp().getTime());

        Assert.assertEquals(expectedString, transactionData.toString());
    }

    @Test
    public void testToBsonDocument() {
        final TransactionData transactionData = getTransactionData(true, true, true, true);
        final BsonDocument bsonDocument = new BsonDocument("transactionId", new BsonString(transactionData.getTransactionId()))
                .append("originalState", new BsonString(transactionData.getOriginalState()))
                .append("newState", new BsonString(transactionData.getNewState()))
                .append("timestamp", new BsonDateTime(transactionData.getTimestamp().getTime()));

        Assert.assertEquals(bsonDocument, transactionData.toBsonDocument());
    }

    @Test
    public void testToBsonDocumentWithoutOriginalState() {
        final TransactionData transactionData = getTransactionData(true, false, true, true);
        final BsonDocument bsonDocument = new BsonDocument("transactionId", new BsonString(transactionData.getTransactionId()))
                .append("originalState", new BsonString("null"))
                .append("newState", new BsonString(transactionData.getNewState()))
                .append("timestamp", new BsonDateTime(transactionData.getTimestamp().getTime()));

        Assert.assertEquals(bsonDocument, transactionData.toBsonDocument());
    }

    @Test
    public void testToBsonDocumentWithoutNewState() {
        final TransactionData transactionData = getTransactionData(true, true, false, true);
        final BsonDocument bsonDocument = new BsonDocument("transactionId", new BsonString(transactionData.getTransactionId()))
                .append("originalState", new BsonString(transactionData.getOriginalState()))
                .append("newState", new BsonString("null"))
                .append("timestamp", new BsonDateTime(transactionData.getTimestamp().getTime()));

        Assert.assertEquals(bsonDocument, transactionData.toBsonDocument());
    }

    @Test
    public void testValueOf() {
        final TransactionData transactionData = getTransactionData(true, true, true, true);
        final TransactionData newTransactionData = TransactionData.valueOf(transactionData.toString());

        Assert.assertEquals(transactionData, newTransactionData);
    }

    @Test
    public void testValueOfWithoutOriginalState() {
        final TransactionData transactionData = getTransactionData(true, false, true, true);
        final TransactionData newTransactionData = TransactionData.valueOf(transactionData.toString());

        Assert.assertEquals(transactionData, newTransactionData);
    }

    @Test
    public void testValueOfWithoutNewState() {
        final TransactionData transactionData = getTransactionData(true, true, false, true);
        final TransactionData newTransactionData = TransactionData.valueOf(transactionData.toString());

        Assert.assertEquals(transactionData, newTransactionData);
    }

    @Test
    public void testValueOfWithStates() {
        final TransactionData transactionData = getTransactionData(true, false, false, true);
        final TransactionData newTransactionData = TransactionData.valueOf(transactionData.toString());

        Assert.assertEquals(transactionData, newTransactionData);
    }

    private TransactionData getTransactionData(final boolean withTransactionId, final boolean withOriginalState,
            final boolean withNewState, final boolean withTimestamp) {

        String transactionId = null;
        String originalState = null;
        String newState = null;
        Date timestamp = null;

        if (withTransactionId) {
            transactionId = UUID.randomUUID().toString();
        }

        if (withOriginalState) {
            originalState = "testOriginalState: " + new BsonString(UUID.randomUUID().toString());
        }

        if (withNewState) {
            newState = "testNewState" + new BsonString(UUID.randomUUID().toString());
        }

        if (withTimestamp) {
            timestamp = new Date();
        }

        return new TransactionData(transactionId, originalState, newState, timestamp);
    }

}
