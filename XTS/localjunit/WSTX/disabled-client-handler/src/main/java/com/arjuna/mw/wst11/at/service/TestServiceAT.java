package com.arjuna.mw.wst11.at.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "TestServiceAT", targetNamespace = "http://arjuna.com/mw/wst11/at/service")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface TestServiceAT {

    @WebMethod
    void increment();

    @WebMethod
    int getCounter();

    @WebMethod
    List<String> getTwoPhaseCommitInvocations();

    @WebMethod
    void reset();

}
