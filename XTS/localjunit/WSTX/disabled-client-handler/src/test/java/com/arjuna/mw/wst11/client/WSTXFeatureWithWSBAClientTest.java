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

import com.arjuna.mw.wst11.UserBusinessActivity;
import com.arjuna.mw.wst11.UserBusinessActivityFactory;
import com.arjuna.mw.wst11.ba.client.TestServiceBAClient;
import com.arjuna.mw.wst11.ba.service.TestServiceBA;
import com.arjuna.mw.wst11.ba.service.TestServiceBAImple;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
@RunWith(Arquillian.class)
public final class WSTXFeatureWithWSBAClientTest {

    private static final String ManifestMF = "Manifest-Version: 1.0\n"
            + "Dependencies: org.jboss.xts,org.jboss.modules,org.jboss.msc,org.jboss.jts\n";

    private UserBusinessActivity uba;

    @Deployment
    public static WebArchive getDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(TestServiceBA.class, TestServiceBAImple.class, TestServiceBAClient.class, WSTXFeature.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsResource("context-handlers.xml").setManifest(new StringAsset(ManifestMF));
    }

    @Before
    public void before() throws Exception {
        uba = UserBusinessActivityFactory.userBusinessActivity();
        uba.begin();
    }

    @After
    public void after() {
        cancelIfActive(uba);
    }

    @Test
    public void testSuccessWithoutFeature() throws Exception {
        TestServiceBAClient client = getClientWithoutFeature();
        client.increment();
        uba.close();
        assertInvocations(client.getBusinessActivityInvocations());
    }

    @Test
    public void testCancelWithoutFeature() throws Exception {
        TestServiceBAClient client = getClientWithoutFeature();
        client.increment();
        uba.cancel();
        assertInvocations(client.getBusinessActivityInvocations());
    }

    @Test
    public void testSuccessWithEnabledFeature() throws Exception {
        TestServiceBAClient client = getClientWithFeature(true);
        client.increment();
        uba.close();
        assertInvocations(client.getBusinessActivityInvocations(), "complete", "confirmCompleted", "close");
    }

    @Test
    public void testCancelWithEnabledFeature() throws Exception {
        TestServiceBAClient client = getClientWithFeature(true);
        client.increment();
        uba.cancel();
        assertInvocations(client.getBusinessActivityInvocations(), "cancel");
    }

    @Test
    public void testSuccessWithDisabledFeature() throws Exception {
        TestServiceBAClient client = getClientWithFeature(false);
        client.increment();
        uba.close();
        assertInvocations(client.getBusinessActivityInvocations());
    }

    @Test
    public void testCancelWithDisabledFeature() throws Exception {
        TestServiceBAClient client = getClientWithFeature(false);
        client.increment();
        uba.cancel();
        assertInvocations(client.getBusinessActivityInvocations());
    }

    private TestServiceBAClient getClientWithoutFeature() throws Exception {
        TestServiceBAClient client = new TestServiceBAClient();
        client.reset();

        return client;
    }

    private TestServiceBAClient getClientWithFeature(final boolean isEnableWSTXFeature) throws Exception {
        TestServiceBAClient client = new TestServiceBAClient(isEnableWSTXFeature);
        client.reset();

        return client;
    }

    private void assertInvocations(List<String> actual, String... expected) {
        Assert.assertArrayEquals(expected, actual.toArray());
    }

    private void cancelIfActive(UserBusinessActivity uba) {
        try {
            uba.cancel();
        } catch (Throwable t) {
        }
    }

}
