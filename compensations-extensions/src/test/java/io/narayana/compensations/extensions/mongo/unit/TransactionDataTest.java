package io.narayana.compensations.extensions.mongo.unit;

import io.narayana.compensations.extensions.mongo.TransactionData;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TransactionDataTest {

    @Test
    public void testToStringWithNulls() {
        final TransactionData transactionData = new TransactionData();

        Assert.assertEquals("<TransactionData: transactionId=null, originalState=null, newState=null, timestamp=null>",
                transactionData.toString());
    }

    @Test
    public void testToString() {
        final Date date = new Date();
        final TransactionData transactionData = new TransactionData("dummyTransactionData", "dummyOriginalState",
                "dummyNewState", date);

        Assert.assertEquals("<TransactionData: transactionId=dummyTransactionData, originalState=dummyOriginalState, " +
                "newState=dummyNewState, timestamp=" + date.getTime() + ">", transactionData.toString());
    }

    @Test
    public void testValueOf() {
        final TransactionData transactionData = new TransactionData("dummyTransactionData", "dummyOriginalState",
                "dummyNewState", new Date());
        final TransactionData newTransactionData = TransactionData.valueOf(transactionData.toString());

        Assert.assertEquals(transactionData, newTransactionData);
    }

    @Test
    public void testValueOfWithNullStates() {
        final TransactionData transactionData = new TransactionData("dummyTransactionData", null, null, new Date());
        final TransactionData newTransactionData = TransactionData.valueOf(transactionData.toString());

        Assert.assertEquals(transactionData, newTransactionData);
    }

}
