package io.narayana.compensations.extensions.mongo.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@RunWith(Arquillian.class)
public class CompensationsTest {

    final static String MONGO_DRIVER_COORDINATES = "org.mongodb:mongo-java-driver:"
            + System.getProperty("version.org.mongodb");

    @Inject
    private CompensatableService compensatableService;

    @Deployment
    public static WebArchive getDeployment() {
        final File mongoDriver = Maven.resolver().resolve(MONGO_DRIVER_COORDINATES).withoutTransitivity().asSingleFile();

        final WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage("io.narayana.compensations.extensions.mongo")
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
        TestConfirmationHandler.INVOCATIONS_COUNTER.set(0);
        TestCompensationHandler.INVOCATIONS_COUNTER.set(0);
    }

    @Test
    public void test() {
        compensatableService.execute();

        Assert.assertEquals(1, CompensatableService.INVOCATIONS_COUNTER.get());
        Assert.assertEquals(2, TestConfirmationHandler.INVOCATIONS_COUNTER.get());
        Assert.assertEquals(0, TestCompensationHandler.INVOCATIONS_COUNTER.get());
    }

}
