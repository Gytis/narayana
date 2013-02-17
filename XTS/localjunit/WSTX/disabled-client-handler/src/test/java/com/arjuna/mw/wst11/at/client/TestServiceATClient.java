package com.arjuna.mw.wst11.at.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.arjuna.mw.wst11.at.service.TestServiceAT;
import com.arjuna.mw.wst11.client.WSTXFeature;

public final class TestServiceATClient implements TestServiceAT {

    private final TestServiceAT testService;

    public TestServiceATClient() throws MalformedURLException {
        URL wsdlLocation = new URL("http://localhost:8080/test/TestServiceATService/TestServiceAT?wsdl");
        QName serviceName = new QName("http://arjuna.com/mw/wst11/at/service", "TestServiceATService");
        QName portName = new QName("http://arjuna.com/mw/wst11/at/service", "TestServiceAT");

        Service service = Service.create(wsdlLocation, serviceName);
        testService = service.getPort(portName, TestServiceAT.class);
    }

    public TestServiceATClient(final boolean isEnableWSTXFeature) throws MalformedURLException {
        URL wsdlLocation = new URL("http://localhost:8080/test/TestServiceATService/TestServiceAT?wsdl");
        QName serviceName = new QName("http://arjuna.com/mw/wst11/at/service", "TestServiceATService");
        QName portName = new QName("http://arjuna.com/mw/wst11/at/service", "TestServiceAT");

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
