package io.narayana.compensations.extensions.mongo.integration;

import io.narayana.compensations.extensions.mongo.internal.TransactionData;
import org.bson.Document;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;

import java.io.File;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class BaseTest {

    private static final String MONGO_DRIVER_COORDINATES = "org.mongodb:mongo-java-driver:"
            + System.getProperty("version.org.mongodb");

    private static final String TRANSACTION_DATA_COLUMN_NAME = TransactionData.class.getSimpleName();

    @Deployment
    public static WebArchive getDeployment() {
        final File mongoDriver = Maven.resolver().resolve(MONGO_DRIVER_COORDINATES).withoutTransitivity().asSingleFile();

        final WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage("io.narayana.compensations.extensions.mongo")
                .addPackage("io.narayana.compensations.extensions.mongo.internal")
                .addPackage("io.narayana.compensations.extensions.mongo.internal.handlers")
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

    protected void assertDatabaseEntries(final String transactionId, final DatabaseManager databaseManager,
            final DatabaseEntry... expectedEntries) {

        for (final DatabaseEntry expectedEntry : expectedEntries) {
            assertDatabaseEntry(transactionId, databaseManager, expectedEntry);
        }
    }

    protected void assertDatabaseEntry(final String transactionId, final DatabaseManager databaseManager,
            final DatabaseEntry expectedEntry) {

        for (final Document document : databaseManager.getEntries(expectedEntry)) {
            Assert.assertEquals("Database entry contains unexpected value", expectedEntry.getValue(),
                    document.getString(expectedEntry.getKey()));

            if (transactionId != null) {
                final Object transactionData = document.get(TRANSACTION_DATA_COLUMN_NAME);

                Assert.assertTrue(TRANSACTION_DATA_COLUMN_NAME + " record is of invalid type",
                        transactionData instanceof Document);
                Assert.assertEquals("Transaction ids should match", transactionId,
                        ((Document) transactionData).getString("transactionId"));
            }
        }
    }

}
