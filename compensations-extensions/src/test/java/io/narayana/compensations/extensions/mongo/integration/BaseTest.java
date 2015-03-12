package io.narayana.compensations.extensions.mongo.integration;

import io.narayana.compensations.extensions.mongo.TransactionData;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    protected void assertDatabaseEntriesWithTransactionData(final String key, final List<String> expectedValues,
            final Iterator<Document> iterator, final String transactionId) throws Exception {

        final List<String> remainingValues = new ArrayList<>(expectedValues);

        while (iterator.hasNext()) {
            final Document document = iterator.next();
            final Object txInfo = document.get(TRANSACTION_DATA_COLUMN_NAME);

            Assert.assertTrue(txInfo instanceof Document);
            Assert.assertEquals(transactionId, ((Document) txInfo).getString("transactionId"));

            remainingValues.remove(document.getString(key));
        }

        Assert.assertTrue(remainingValues.isEmpty());
    }

    protected void assertDatabaseEntriesWithoutTransactionData(final String key, final List<String> expectedValues,
            final Iterator<Document> iterator) {

        final List<String> remainingValues = new ArrayList<>(expectedValues);

        while (iterator.hasNext()) {
            final Document document = iterator.next();
            final Object txInfo = document.get(TRANSACTION_DATA_COLUMN_NAME);

            Assert.assertNull(txInfo);

            remainingValues.remove(document.getString(key));
        }

        Assert.assertTrue(remainingValues.isEmpty());
    }

}
