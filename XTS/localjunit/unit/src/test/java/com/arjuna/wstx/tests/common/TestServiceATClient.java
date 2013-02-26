package com.arjuna.wstx.tests.common;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.arjuna.mw.wst11.client.WSTXFeature;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
public final class TestServiceATClient implements TestServiceAT {

    private final TestServiceAT testService;

    public TestServiceATClient() throws MalformedURLException {
        URL wsdlLocation = new URL("http://localhost:8081/test/TestServiceATService/TestServiceAT?wsdl");
        QName serviceName = new QName("http://arjuna.com/wstx/tests/common", "TestServiceATService");
        QName portName = new QName("http://arjuna.com/wstx/tests/common", "TestServiceAT");

        Service service = Service.create(wsdlLocation, serviceName);
        testService = service.getPort(portName, TestServiceAT.class);
    }

    public TestServiceATClient(final boolean isEnableWSTXFeature) throws MalformedURLException {
        URL wsdlLocation = new URL("http://localhost:8081/test/TestServiceATService/TestServiceAT?wsdl");
        QName serviceName = new QName("http://arjuna.com/wstx/tests/common", "TestServiceATService");
        QName portName = new QName("http://arjuna.com/wstx/tests/common", "TestServiceAT");

        Service service = Service.create(wsdlLocation, serviceName);
        testService = service.getPort(portName, TestServiceAT.class, new WSTXFeature(isEnableWSTXFeature));
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