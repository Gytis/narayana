package org.jboss.jbossts.txbridge.tests.outbound.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.jboss.jbossts.txbridge.outbound.JTAToWSATBridgeFeature;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
public final class TestServiceATClient implements TestServiceAT {

    private final TestServiceAT testService;

    public TestServiceATClient() throws MalformedURLException {
        URL wsdlLocation = new URL("http://localhost:8080/test/TestServiceATService/TestServiceAT?wsdl");
        QName serviceName = new QName("http://jboss.org/jbossts/txbridge/tests/outbound", "TestServiceATService");
        QName portName = new QName("http://jboss.org/jbossts/txbridge/tests/outbound", "TestServiceAT");

        Service service = Service.create(wsdlLocation, serviceName);
        testService = service.getPort(portName, TestServiceAT.class);
    }

    public TestServiceATClient(final boolean isEnableJTAToWSATBridgeFeature) throws MalformedURLException {
        URL wsdlLocation = new URL("http://localhost:8080/test/TestServiceATService/TestServiceAT?wsdl");
        QName serviceName = new QName("http://jboss.org/jbossts/txbridge/tests/outbound", "TestServiceATService");
        QName portName = new QName("http://jboss.org/jbossts/txbridge/tests/outbound", "TestServiceAT");

        Service service = Service.create(wsdlLocation, serviceName);
        testService = service.getPort(portName, TestServiceAT.class, new JTAToWSATBridgeFeature(isEnableJTAToWSATBridgeFeature));
    }

    @Override
    public void increment() {
        testService.increment();
    }

    @Override
    public int getCounter() {
        return testService.getCounter();
    }

    @Override
    public List<String> getTwoPhaseCommitInvocations() {
        return testService.getTwoPhaseCommitInvocations();
    }

    @Override
    public void reset() {
        testService.reset();
    }

}