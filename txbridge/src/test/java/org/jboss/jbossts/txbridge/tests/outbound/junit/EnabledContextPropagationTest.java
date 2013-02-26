package org.jboss.jbossts.txbridge.tests.outbound.junit;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jbossts.txbridge.outbound.JTAToWSATBridgeFeature;
import org.jboss.jbossts.txbridge.tests.outbound.client.TestServiceAT;
import org.jboss.jbossts.txbridge.tests.outbound.client.TestServiceATClient;
import org.jboss.jbossts.txbridge.tests.outbound.service.TestServiceATImple;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
@RunWith(Arquillian.class)
public final class EnabledContextPropagationTest {

    private static final String ManifestMF = "Manifest-Version: 1.0\n"
            + "Dependencies: org.jboss.modules,deployment.arquillian-service,org.jboss.msc,org.jboss.jts,org.jboss.xts\n";

    @Deployment
    public static WebArchive getDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(TestServiceAT.class, TestServiceATImple.class, TestServiceATClient.class, JTAToWSATBridgeFeature.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("outbound/jaxws-handlers-server.xml", "jaxws-handlers-server.xml")
                .setManifest(new StringAsset(ManifestMF));
    }

    @After
    public void after() {
        rollbackIfActive();
    }

    /**
     * Tests commit without WSTXFeature and with enabled WSTX handler.
     *
     * Prepare and commit calls are expected.
     *
     * @throws Exception
     */
    @Test
    public void testCommitWithoutFeature() throws Exception {
        TestServiceATClient client = getClientWithoutFeature();
        beginTransaction();
        client.increment();
        commitTransaction();
        assertInvocations(client.getTwoPhaseCommitInvocations(), "prepare", "commit");
    }

    private TestServiceATClient getClientWithoutFeature() throws Exception {
        TestServiceATClient client = new TestServiceATClient();
        client.reset();

        return client;
    }

    private void assertInvocations(List<String> actual, String... expected) {
        Assert.assertArrayEquals(expected, actual.toArray());
    }

    private UserTransaction getTransaction() throws NamingException {
        Context context = new InitialContext();
        return (UserTransaction) context.lookup("java:comp/UserTransaction");
    }

    private void beginTransaction() throws NamingException, NotSupportedException, SystemException {
        UserTransaction ut = getTransaction();
        ut.begin();
    }

    private void commitTransaction() throws NamingException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException {
        UserTransaction ut = getTransaction();
        ut.commit();
    }

    private void rollbackTransaction() throws IllegalStateException, SecurityException, SystemException, NamingException {
        UserTransaction ut = getTransaction();
        ut.rollback();
    }

    private void rollbackIfActive() {
        try {
            rollbackTransaction();
        } catch (Throwable t) {
        }
    }
}
