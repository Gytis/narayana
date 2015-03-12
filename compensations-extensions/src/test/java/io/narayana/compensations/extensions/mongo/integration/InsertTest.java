package io.narayana.compensations.extensions.mongo.integration;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.narayana.compensations.impl.BAControllerFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@RunWith(Arquillian.class)
public class InsertTest extends BaseTest {

    @Inject
    private DatabaseManager databaseManager;

    @Before
    public void before() {
        databaseManager.clearEntries();
        Assert.assertFalse(databaseManager.getEntries().hasNext());
    }

    @After
    public void after() throws Exception {
        if (BAControllerFactory.getInstance().isBARunning()) {
            BAControllerFactory.getInstance().cancelBusinessActivity();
        }

        databaseManager.clearEntries();
    }

    @Test
    public void testSuccess() throws Exception {
        final String key = "test";
        final List<String> values = Arrays.asList("1", "2");

        BAControllerFactory.getInstance().beginBusinessActivity();

        databaseManager.insert(key, values.get(0));
        databaseManager.insert(key, values.get(1));

        final String transactionId = BAControllerFactory.getInstance().getCurrentTransaction().toString();
        assertDatabaseEntriesWithTransactionData(key, values, databaseManager.getEntries(), transactionId);

        BAControllerFactory.getInstance().closeBusinessActivity();

        assertDatabaseEntriesWithoutTransactionData(key, values, databaseManager.getEntries());
    }

    @Test
    public void testCompensate() throws Exception {
        final String key = "test";
        final List<String> values = Arrays.asList("3", "4");

        BAControllerFactory.getInstance().beginBusinessActivity();

        databaseManager.insert(key, values.get(0));
        databaseManager.insert(key, values.get(1));

        final String transactionId = BAControllerFactory.getInstance().getCurrentTransaction().toString();
        assertDatabaseEntriesWithTransactionData(key, values, databaseManager.getEntries(), transactionId);

        BAControllerFactory.getInstance().cancelBusinessActivity();

        Assert.assertFalse("Entries should be removed by compensation handler", databaseManager.getEntries().hasNext());
    }

    @Test
    public void testCompensateModifiedDocument() throws Exception {
        final String key = "test";
        final List<String> values = Arrays.asList("5", "6");

        BAControllerFactory.getInstance().beginBusinessActivity();

        databaseManager.insert(key, values.get(0));
        databaseManager.insert(key, values.get(1));

        final String transactionId = BAControllerFactory.getInstance().getCurrentTransaction().toString();
        assertDatabaseEntriesWithTransactionData(key, values, databaseManager.getEntries(), transactionId);

        databaseManager.updateWithoutTransaction(key, values.get(1), key, "7");

        BAControllerFactory.getInstance().cancelBusinessActivity();

        assertDatabaseEntriesWithTransactionData(key, Arrays.asList("7"), databaseManager.getEntries(), transactionId);
    }

}
