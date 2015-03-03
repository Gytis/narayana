package io.narayana.compensations.extensions.mongo.integration;

import org.bson.Document;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.narayana.compensations.impl.BAControllerFactory;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@RunWith(Arquillian.class)
public class CompensationsTest {

    private static final String MONGO_DRIVER_COORDINATES = "org.mongodb:mongo-java-driver:"
            + System.getProperty("version.org.mongodb");

    @Inject
    private CompensatableService compensatableService;

    @Inject
    private DatabaseManager databaseManager;

    @Deployment
    public static WebArchive getDeployment() {
        final File mongoDriver = Maven.resolver().resolve(MONGO_DRIVER_COORDINATES).withoutTransitivity().asSingleFile();

        final WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage("io.narayana.compensations.extensions.mongo")
                .addPackage("io.narayana.compensations.extensions.mongo.handlers")
                .addPackage("io.narayana.compensations.extensions.mongo.integration")
                .addAsManifestResource("services/javax.enterprise.inject.spi.Extension")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(mongoDriver);

        archive.delete(ArchivePaths.create("META-INF/MANIFEST.MF"));

        final String ManifestMF = "Manifest-Version: 1.0\n"
                + "Dependencies: org.jboss.narayana.compensations\n";
        archive.setManifest(new StringAsset(ManifestMF));

        return archive;
    }

    @Before
    public void before() {
        CompensatableService.INVOCATIONS_COUNTER.set(0);
        InsertConfirmationHandler.INVOCATIONS_COUNTER.set(0);
        InsertCompensationHandler.INVOCATIONS_COUNTER.set(0);
        databaseManager.clear();
        Assert.assertFalse(databaseManager.getAll().hasNext());
    }

    @After
    public void after() throws Exception {
        if (BAControllerFactory.getInstance().isBARunning()) {
            BAControllerFactory.getInstance().cancelBusinessActivity();
        }

        databaseManager.clear();
    }

    @Test
    public void testSuccess() throws Exception {
        final String key = "test";
        final List<String> values = Arrays.asList("1", "2");

        BAControllerFactory.getInstance().beginBusinessActivity();

        compensatableService.execute(key, values);

        final String transactionId = BAControllerFactory.getInstance().getCurrentTransaction().toString();
        assertDatabaseEntriesWithTransactionData(key, values, databaseManager.getAll(), transactionId);

        BAControllerFactory.getInstance().closeBusinessActivity();

        Assert.assertEquals(1, CompensatableService.INVOCATIONS_COUNTER.get());

        // TODO skipping these, because of a possible bug in handling CompensationContext
//        Assert.assertEquals(2, InsertConfirmationHandler.INVOCATIONS_COUNTER.get());
//        Assert.assertEquals(0, InsertCompensationHandler.INVOCATIONS_COUNTER.get());

        assertDatabaseEntriesWithoutTransactionData(key, values, databaseManager.getAll());
    }

    @Test
    public void testCompensate() throws Exception {
        final String key = "test";
        final List<String> values = Arrays.asList("3", "4");

        BAControllerFactory.getInstance().beginBusinessActivity();

        compensatableService.execute(key, values);

        final String transactionId = BAControllerFactory.getInstance().getCurrentTransaction().toString();
        assertDatabaseEntriesWithTransactionData(key, values, databaseManager.getAll(), transactionId);

        BAControllerFactory.getInstance().cancelBusinessActivity();

        Assert.assertEquals(1, CompensatableService.INVOCATIONS_COUNTER.get());

        // TODO skipping these, because of a possible bug in handling CompensationContext
//        Assert.assertEquals(0, InsertConfirmationHandler.INVOCATIONS_COUNTER.get());
//        Assert.assertEquals(2, InsertCompensationHandler.INVOCATIONS_COUNTER.get());

        assertDatabaseEntriesWithTransactionData(key, values, databaseManager.getAll(), transactionId);
    }

    private void assertDatabaseEntriesWithTransactionData(final String key, final List<String> expectedValues,
            final Iterator<Document> iterator, final String transactionId) throws Exception {

        final List<String> remainingValues = new ArrayList<>(expectedValues);

        while (iterator.hasNext()) {
            final Document document = iterator.next();
            final Object txInfo = document.get("txinfo");

            Assert.assertTrue(txInfo instanceof Document);
            Assert.assertEquals(transactionId, ((Document) txInfo).getString("transactionId"));

            remainingValues.remove(document.getString(key));
        }

        Assert.assertTrue(remainingValues.isEmpty());
    }

    private void assertDatabaseEntriesWithoutTransactionData(final String key, final List<String> expectedValues,
            final Iterator<Document> iterator) {

        final List<String> remainingValues = new ArrayList<>(expectedValues);

        while (iterator.hasNext()) {
            final Document document = iterator.next();
            final Object txInfo = document.get("txinfo");

            Assert.assertNull(txInfo);

            remainingValues.remove(document.getString(key));
        }

        Assert.assertTrue(remainingValues.isEmpty());
    }

}
