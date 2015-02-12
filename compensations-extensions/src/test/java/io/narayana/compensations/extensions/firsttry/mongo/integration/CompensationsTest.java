package io.narayana.compensations.extensions.firsttry.mongo.integration;

import io.narayana.compensations.extensions.firsttry.CompensatableOperation;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@RunWith(Arquillian.class)
public class CompensationsTest {

    final static String mongoDriverVersion = System.getProperty("version.org.mongodb");

    @Deployment
    public static WebArchive getDeployment() {
        final String mongoDriverDependencyUrl = "org.mongodb:mongo-java-driver:3.0.0-beta1";
        Maven.resolver().loadPomFromFile("pom.xml");
//        final File lib = Maven.resolver().loadPomFromFile("pom.xml").resolve(mongoDriverDependencyUrl)
//                .withoutTransitivity().asSingleFile();

        final WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "io.narayana.compensations.extensions")
                .addClass(TestClass.class)
                .addAsManifestResource("services/javax.enterprise.inject.spi.Extension")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
//                .addAsLibraries(lib);

        return archive;
    }

    @Test
    public void test() {
        TestClass tc = new TestClass();
        tc.execute();
    }

    public class TestClass {
        @CompensatableOperation
        public void execute() {
            System.out.println("Executing");
        }
    }

}
