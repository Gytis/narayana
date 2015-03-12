package io.narayana.compensations.extensions.mongo.unit;

//import io.narayana.compensations.extensions.mongo.TransactionData;
//import org.bson.BsonDateTime;
//import org.bson.BsonDocument;
//import org.bson.BsonString;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.util.Date;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TransactionDataTest {

//    @Test
//    public void testToStringWithNulls() {
//        final TransactionData transactionData = new TransactionData(null, null, null, null);
//
//        Assert.assertEquals("<TransactionData: transactionId=null, originalState=null, newState=null, timestamp=null>",
//                transactionData.toString());
//    }
//
//    @Test
//    public void testToString() {
//        final Date date = new Date();
//        final TransactionData transactionData = new TransactionData("dummyTransactionData", "dummyOriginalState",
//                "dummyNewState", date);
//
//        Assert.assertEquals("<TransactionData: transactionId=dummyTransactionData, originalState=dummyOriginalState, " +
//                "newState=dummyNewState, timestamp=" + date.getTime() + ">", transactionData.toString());
//    }
//
//    @Test
//    public void testToStringWithoutOriginalState() {
//        final Date date = new Date();
//        final TransactionData transactionData = new TransactionData("dummyTransactionData", null,
//                "dummyNewState", date);
//
//        Assert.assertEquals("<TransactionData: transactionId=dummyTransactionData, originalState=null, " +
//                "newState=dummyNewState, timestamp=" + date.getTime() + ">", transactionData.toString());
//    }
//
//    @Test
//    public void testToStringWithoutNewState() {
//        final Date date = new Date();
//        final TransactionData transactionData = new TransactionData("dummyTransactionData", "dummyOriginalState", null,
//                date);
//
//        Assert.assertEquals("<TransactionData: transactionId=dummyTransactionData, originalState=dummyOriginalState, " +
//                "newState=null, timestamp=" + date.getTime() + ">", transactionData.toString());
//    }
//
//    @Test
//    public void testToBsonDocument() {
//        final Date date = new Date();
//        final TransactionData transactionData = new TransactionData("dummyTransactionData", "dummyOriginalState",
//                "dummyNewState", date);
//        final BsonDocument bsonDocument = new BsonDocument("transactionId", new BsonString("dummyTransactionData"))
//                .append("originalState", new BsonString("dummyOriginalState"))
//                .append("newState", new BsonString("dummyNewState"))
//                .append("timestamp", new BsonDateTime(date.getTime()));
//
//        Assert.assertEquals(bsonDocument, transactionData.toBsonDocument());
//    }
//
//    @Test
//    public void testToBsonDocumentWithoutOriginalState() {
//        final Date date = new Date();
//        final TransactionData transactionData = new TransactionData("dummyTransactionData", null, "dummyNewState", date);
//        final BsonDocument bsonDocument = new BsonDocument("transactionId", new BsonString("dummyTransactionData"))
//                .append("originalState", new BsonString("null"))
//                .append("newState", new BsonString("dummyNewState"))
//                .append("timestamp", new BsonDateTime(date.getTime()));
//
//        Assert.assertEquals(bsonDocument, transactionData.toBsonDocument());
//    }
//
//    @Test
//    public void testToBsonDocumentWithoutNewState() {
//        final Date date = new Date();
//        final TransactionData transactionData = new TransactionData("dummyTransactionData", "dummyOriginalState", null,
//                date);
//        final BsonDocument bsonDocument = new BsonDocument("transactionId", new BsonString("dummyTransactionData"))
//                .append("originalState", new BsonString("dummyOriginalState"))
//                .append("newState", new BsonString("null"))
//                .append("timestamp", new BsonDateTime(date.getTime()));
//
//        Assert.assertEquals(bsonDocument, transactionData.toBsonDocument());
//    }
//
//    @Test
//    public void testValueOf() {
//        final TransactionData transactionData = new TransactionData("dummyTransactionData", "dummyOriginalState",
//                "dummyNewState", new Date());
//        final TransactionData newTransactionData = TransactionData.valueOf(transactionData.toString());
//
//        Assert.assertEquals(transactionData, newTransactionData);
//    }
//
//    @Test
//    public void testValueOfWithoutOriginalState() {
//        final TransactionData transactionData = new TransactionData("dummyTransactionData", null, "dummyNewState",
//                new Date());
//        final TransactionData newTransactionData = TransactionData.valueOf(transactionData.toString());
//
//        Assert.assertEquals(transactionData, newTransactionData);
//    }
//
//    @Test
//    public void testValueOfWithoutNewState() {
//        final TransactionData transactionData = new TransactionData("dummyTransactionData", "dummyOriginalState", null,
//                new Date());
//        final TransactionData newTransactionData = TransactionData.valueOf(transactionData.toString());
//
//        Assert.assertEquals(transactionData, newTransactionData);
//    }
//
//    @Test
//    public void testValueOfWithStates() {
//        final TransactionData transactionData = new TransactionData("dummyTransactionData", null, null, new Date());
//        final TransactionData newTransactionData = TransactionData.valueOf(transactionData.toString());
//
//        Assert.assertEquals(transactionData, newTransactionData);
//    }

}
