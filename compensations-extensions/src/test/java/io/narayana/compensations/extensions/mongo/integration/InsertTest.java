package io.narayana.compensations.extensions.mongo.integration;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.narayana.compensations.impl.BAControllerFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

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
        Assert.assertEquals(0, databaseManager.getCount());
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
        final DatabaseEntry firstEntry = new DatabaseEntry("test", "1");
        final DatabaseEntry secondEntry = new DatabaseEntry("test", "2");

        BAControllerFactory.getInstance().beginBusinessActivity();

        databaseManager.insert(firstEntry);
        databaseManager.insert(secondEntry);

        final String transactionId = BAControllerFactory.getInstance().getCurrentTransaction().toString();

        Assert.assertEquals(2, databaseManager.getCount());
        assertDatabaseEntries(transactionId, databaseManager, firstEntry, secondEntry);

        BAControllerFactory.getInstance().closeBusinessActivity();

        Assert.assertEquals(2, databaseManager.getCount());
        assertDatabaseEntries(null, databaseManager, firstEntry, secondEntry);
    }

    @Test
    public void testCompensate() throws Exception {
        final DatabaseEntry firstEntry = new DatabaseEntry("test", "3");
        final DatabaseEntry secondEntry = new DatabaseEntry("test", "4");

        BAControllerFactory.getInstance().beginBusinessActivity();

        databaseManager.insert(firstEntry);
        databaseManager.insert(secondEntry);

        final String transactionId = BAControllerFactory.getInstance().getCurrentTransaction().toString();

        Assert.assertEquals(2, databaseManager.getCount());
        assertDatabaseEntries(transactionId, databaseManager, firstEntry, secondEntry);

        BAControllerFactory.getInstance().cancelBusinessActivity();

        Assert.assertEquals("Entries should be removed by compensation handler", 0, databaseManager.getCount());
    }

    @Test
    public void testCompensateModifiedDocument() throws Exception {
        final DatabaseEntry firstEntry = new DatabaseEntry("test", "5");
        final DatabaseEntry secondEntry = new DatabaseEntry("test", "6");
        final DatabaseEntry thirdEntry = new DatabaseEntry("test", "7");

        BAControllerFactory.getInstance().beginBusinessActivity();

        databaseManager.insert(firstEntry);
        databaseManager.insert(secondEntry);

        final String transactionId = BAControllerFactory.getInstance().getCurrentTransaction().toString();

        Assert.assertEquals(2, databaseManager.getCount());
        assertDatabaseEntries(transactionId, databaseManager, firstEntry, secondEntry);

        databaseManager.updateWithoutTransaction(secondEntry, thirdEntry);

        BAControllerFactory.getInstance().cancelBusinessActivity();

        Assert.assertEquals(1, databaseManager.getCount());
        assertDatabaseEntries(transactionId, databaseManager, thirdEntry);
    }

}
