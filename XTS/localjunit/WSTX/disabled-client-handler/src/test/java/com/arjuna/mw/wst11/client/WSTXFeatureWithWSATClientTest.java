package com.arjuna.mw.wst11.client;

import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.arjuna.mw.wst11.UserTransaction;
import com.arjuna.mw.wst11.UserTransactionFactory;
import com.arjuna.mw.wst11.at.client.TestServiceATClient;
import com.arjuna.mw.wst11.at.service.TestServiceAT;
import com.arjuna.mw.wst11.at.service.TestServiceATImple;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
@RunWith(Arquillian.class)
public final class WSTXFeatureWithWSATClientTest {

    private static final String ManifestMF = "Manifest-Version: 1.0\n"
            + "Dependencies: org.jboss.xts,org.jboss.modules,org.jboss.msc,org.jboss.jts\n";

    private UserTransaction ut;

    @Deployment
    public static WebArchive getDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(TestServiceAT.class, TestServiceATImple.class, TestServiceATClient.class, WSTXFeature.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsResource("context-handlers.xml").setManifest(new StringAsset(ManifestMF));
    }

    @Before
    public void before() throws Exception {
        ut = UserTransactionFactory.userTransaction();
        ut.begin();
    }

    @After
    public void after() {
        rollbackIfActive(ut);
    }

    @Test
    public void testCommitWithoutFeature() throws Exception {
        TestServiceATClient client = getClientWithoutFeature();
        client.increment();
        ut.commit();
        assertInvocations(client.getTwoPhaseCommitInvocations());
    }

    @Test
    public void testRollbackWithoutFeature() throws Exception {
        TestServiceATClient client = getClientWithoutFeature();
        client.increment();
        ut.rollback();
        assertInvocations(client.getTwoPhaseCommitInvocations());
    }

    @Test
    public void testCommitWithEnabledFeature() throws Exception {
        TestServiceATClient client = getClientWithFeature(true);
        client.increment();
        ut.commit();
        assertInvocations(client.getTwoPhaseCommitInvocations(), "prepare", "commit");
    }

    @Test
    public void testRollbackWithEnabledFeature() throws Exception {
        TestServiceATClient client = getClientWithFeature(true);
        client.increment();
        ut.rollback();
        assertInvocations(client.getTwoPhaseCommitInvocations(), "rollback");
    }

    @Test
    public void testCommitWithDisabledFeature() throws Exception {
        TestServiceATClient client = getClientWithFeature(false);
        client.increment();
        ut.commit();
        assertInvocations(client.getTwoPhaseCommitInvocations());
    }

    @Test
    public void testRollbackWithDisabledFeature() throws Exception {
        TestServiceATClient client = getClientWithFeature(false);
        client.increment();
        ut.rollback();
        assertInvocations(client.getTwoPhaseCommitInvocations());
    }

    private TestServiceATClient getClientWithoutFeature() throws Exception {
        TestServiceATClient client = new TestServiceATClient();
        client.reset();

        return client;
    }

    private TestServiceATClient getClientWithFeature(final boolean isEnableWSTXFeature) throws Exception {
        TestServiceATClient client = new TestServiceATClient(isEnableWSTXFeature);
        client.reset();

        return client;
    }

    private void assertInvocations(List<String> actual, String... expected) {
        Assert.assertArrayEquals(expected, actual.toArray());
    }

    private void rollbackIfActive(UserTransaction ut) {
        try {
            ut.rollback();
        } catch (Throwable t) {
        }
    }

}