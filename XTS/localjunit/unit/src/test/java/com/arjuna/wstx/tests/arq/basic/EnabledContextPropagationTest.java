package com.arjuna.wstx.tests.arq.basic;

import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.arjuna.mw.wst11.UserTransaction;
import com.arjuna.mw.wst11.UserTransactionFactory;
import com.arjuna.mw.wst11.client.WSTXFeature;
import com.arjuna.wstx.tests.arq.WarDeployment;
import com.arjuna.wstx.tests.common.TestServiceAT;
import com.arjuna.wstx.tests.common.TestServiceATClient;
import com.arjuna.wstx.tests.common.TestServiceATImple;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
@RunWith(Arquillian.class)
public final class EnabledContextPropagationTest {

    private UserTransaction ut;

    @Deployment
    public static WebArchive getDeployment() {
        return WarDeployment.getDeployment(TestServiceAT.class, TestServiceATImple.class, TestServiceATClient.class,
                WSTXFeature.class).addAsResource("context-handlers.xml");
    }

    @After
    public void after() {
        rollbackIfActive(ut);
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
        ut.commit();
        assertInvocations(client.getTwoPhaseCommitInvocations(), "prepare", "commit");
    }

    /**
     * Tests rollback without WSTXFeature and with enabled WSTX handler.
     *
     * Rollback call is expected.
     *
     * @throws Exception
     */
    @Test
    public void testRollbackWithoutFeature() throws Exception {
        TestServiceATClient client = getClientWithoutFeature();
        beginTransaction();
        client.increment();
        ut.rollback();
        assertInvocations(client.getTwoPhaseCommitInvocations(), "rollback");
    }

    /**
     * Tests service invocation without transaction context, WSTXFeature, and with enabled WSTX handler.
     *
     * No two phase commit calls are expected.
     *
     * @throws Exception
     */
    @Test
    public void testNoTransactionWithoutFeature() throws Exception {
        TestServiceATClient client = new TestServiceATClient();
        client.reset();
        client.increment();
        assertInvocations(client.getTwoPhaseCommitInvocations());
    }

    /**
     * Tests invocation to the non-transactional service without transaction context, WSTXFeature, and with enabled WSTX handler.
     *
     * No two phase commit calls and no exception is expected.
     */
    @Test
    public void testNonTransactionalServiceWithoutFeature() {
        Assert.fail("Not implemented");
    }

    /**
     * Tests commit with enabled WSTXFeature and with enabled WSTX handler.
     *
     * Prepare and commit calls are expected.
     *
     * @throws Exception
     */
    @Test
    public void testCommitWithEnabledFeature() throws Exception {
        TestServiceATClient client = getClientWithFeature(true);
        beginTransaction();
        client.increment();
        ut.commit();
        assertInvocations(client.getTwoPhaseCommitInvocations(), "prepare", "commit");
    }

    /**
     * Tests rollback with enabled WSTXFeature and with enabled WSTX handler.
     *
     * Rollback call is expected.
     *
     * @throws Exception
     */
    @Test
    public void testRollbackWithEnabledFeature() throws Exception {
        TestServiceATClient client = getClientWithFeature(true);
        beginTransaction();
        client.increment();
        ut.rollback();
        assertInvocations(client.getTwoPhaseCommitInvocations(), "rollback");
    }

    /**
     * Tests service invocation without transaction context but with enabled WSTXFeature and with enabled WSTX handler.
     *
     * No two phase commit calls are expected.
     *
     * @throws Exception
     */
    @Test
    public void testNoTransactionWithEnabledFeature() throws Exception {
        TestServiceATClient client = new TestServiceATClient(true);
        client.reset();
        client.increment();
        assertInvocations(client.getTwoPhaseCommitInvocations());
    }

    /**
     * Tests invocation to the non-transactional service without transaction context but with enabled WSTXFeature, and with enabled WSTX handler.
     *
     * Exception is expected.
     */
    @Test
    public void testNonTransactionalServiceWithEnabledFeature() {
        Assert.fail("Not implemented");
    }

    /**
     * Tests commit with disabled WSTXFeature and with enabled WSTX handler.
     *
     * No two phase commit calls are expected.
     *
     * @throws Exception
     */
    @Test
    public void testCommitWithDisabledFeature() throws Exception {
        TestServiceATClient client = getClientWithFeature(false);
        beginTransaction();
        client.increment();
        ut.commit();
        assertInvocations(client.getTwoPhaseCommitInvocations());
    }

    /**
     * Tests rollback with disabled WSTXFeature and with enabled WSTX handler.
     *
     * No two phase commit calls are expected.
     *
     * @throws Exception
     */
    @Test
    public void testRollbackWithDisabledFeature() throws Exception {
        TestServiceATClient client = getClientWithFeature(false);
        beginTransaction();
        client.increment();
        ut.rollback();
        assertInvocations(client.getTwoPhaseCommitInvocations());
    }

    /**
     * Tests service invocation without transaction context but with disabled WSTXFeature and enabled WSTX handler.
     *
     * No two phase commit calls are expected.
     *
     * @throws Exception
     */
    @Test
    public void testNoTransactionWithDisabledFeature() throws Exception {
        TestServiceATClient client = new TestServiceATClient(false);
        client.reset();
        client.increment();
        assertInvocations(client.getTwoPhaseCommitInvocations());
    }

    /**
     * Tests invocation to the non-transactional service without transaction context but with disabled WSTXFeature, and with enabled WSTX handler.
     *
     * No two phase commit calls and no exceptions are expected.
     */
    @Test
    public void testNonTransactionalServiceWithDisabledFeature() {
        Assert.fail("Not implemented");
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

    private void beginTransaction() throws Exception {
        ut = UserTransactionFactory.userTransaction();
        ut.begin();
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
