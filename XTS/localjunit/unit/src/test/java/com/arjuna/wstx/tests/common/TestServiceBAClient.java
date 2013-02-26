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
public final class TestServiceBAClient implements TestServiceBA {

    private final TestServiceBA testService;

    public TestServiceBAClient() throws MalformedURLException {
        URL wsdlLocation = new URL("http://localhost:8080/test/TestServiceBAService/TestServiceBA?wsdl");
        QName serviceName = new QName("http://arjuna.com/wstx/tests/common", "TestServiceBAService");
        QName portName = new QName("http://arjuna.com/wstx/tests/common", "TestServiceBA");

        Service service = Service.create(wsdlLocation, serviceName);
        testService = service.getPort(portName, TestServiceBA.class);
    }

    public TestServiceBAClient(final boolean isEnableWSTXFeature) throws MalformedURLException {
        URL wsdlLocation = new URL("http://localhost:8080/test/TestServiceBAService/TestServiceBA?wsdl");
        QName serviceName = new QName("http://arjuna.com/wstx/tests/common", "TestServiceBAService");
        QName portName = new QName("http://arjuna.com/wstx/tests/common", "TestServiceBA");

        Service service = Service.create(wsdlLocation, serviceName);
        testService = service.getPort(portName, TestServiceBA.class, new WSTXFeature(isEnableWSTXFeature));
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
    public List<String> getBusinessActivityInvocations() {
        return testService.getBusinessActivityInvocations();
    }

    @Override
    public void reset() {
        testService.reset();
    }

}