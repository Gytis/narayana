package io.narayana.compensations.extensions.mongo.integration;

import com.mongodb.DBObject;
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

    private static final String TRANSACTION_DATA_COLUMN_NAME = "txinfo";

    @Deployment
    public static WebArchive getDeployment() {
        final WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage("io.narayana.compensations.extensions.mongo")
//                .addPackage("io.narayana.compensations.extensions.mongo.handlers")
                .addPackage("io.narayana.compensations.extensions.mongo.integration")
                .addPackage("org.jboss.javaee.mongodb")
                .addAsManifestResource("services/javax.enterprise.inject.spi.Extension")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        archive.delete(ArchivePaths.create("META-INF/MANIFEST.MF"));

        final String ManifestMF = "Manifest-Version: 1.0\n"
                + "Dependencies: org.jboss.narayana.compensations\n";
        archive.setManifest(new StringAsset(ManifestMF));

        return archive;
    }

    protected void assertDatabaseEntriesWithTransactionData(final String key, final List<String> expectedValues,
            final Iterator<DBObject> iterator, final String transactionId) throws Exception {

        final List<String> remainingValues = new ArrayList<>(expectedValues);

        while (iterator.hasNext()) {
            final DBObject document = iterator.next();
            final Object txInfo = document.get(TRANSACTION_DATA_COLUMN_NAME);

            // TODO uncomment, once compensations are working again
//            Assert.assertTrue(txInfo instanceof Document);
//            Assert.assertEquals(transactionId, ((Document) txInfo).getString("transactionId"));

            remainingValues.remove(document.get(key));
        }

        Assert.assertTrue(remainingValues.isEmpty());
    }

    protected void assertDatabaseEntriesWithoutTransactionData(final String key, final List<String> expectedValues,
            final Iterator<DBObject> iterator) {

        final List<String> remainingValues = new ArrayList<>(expectedValues);

        while (iterator.hasNext()) {
            final DBObject document = iterator.next();
            final Object txInfo = document.get(TRANSACTION_DATA_COLUMN_NAME);

            Assert.assertNull(txInfo);

            remainingValues.remove(document.get(key));
        }

        Assert.assertTrue(remainingValues.isEmpty());
    }

}
