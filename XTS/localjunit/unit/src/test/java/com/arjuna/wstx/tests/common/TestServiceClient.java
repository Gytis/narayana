package com.arjuna.wstx.tests.common;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.arjuna.mw.wst11.client.WSTXFeature;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
public final class TestServiceClient implements TestService {

    private final TestService testService;

    public TestServiceClient() throws MalformedURLException {
        URL wsdlLocation = new URL("http://localhost:8080/test/TestServiceService/TestService?wsdl");
        QName serviceName = new QName("http://arjuna.com/wstx/tests/common", "TestServiceService");
        QName portName = new QName("http://arjuna.com/wstx/tests/common", "TestService");

        Service service = Service.create(wsdlLocation, serviceName);
        testService = service.getPort(portName, TestService.class);
    }

    public TestServiceClient(final boolean isEnableWSTXFeature) throws MalformedURLException {
        URL wsdlLocation = new URL("http://localhost:8080/test/TestServiceService/TestService?wsdl");
        QName serviceName = new QName("http://arjuna.com/wstx/tests/common", "TestServiceService");
        QName portName = new QName("http://arjuna.com/wstx/tests/common", "TestService");

        Service service = Service.create(wsdlLocation, serviceName);
        testService = service.getPort(portName, TestService.class, new WSTXFeature(isEnableWSTXFeature));
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
    public void reset() {
        testService.reset();
    }

}