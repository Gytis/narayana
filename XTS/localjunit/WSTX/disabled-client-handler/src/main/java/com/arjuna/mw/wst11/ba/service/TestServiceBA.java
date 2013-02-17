package com.arjuna.mw.wst11.ba.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "TestServiceBA", targetNamespace = "http://arjuna.com/mw/wst11/ba/service")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface TestServiceBA {

    @WebMethod
    void increment();

    @WebMethod
    int getCounter();

    @WebMethod
    List<String> getBusinessActivityInvocations();

    @WebMethod
    void reset();

}