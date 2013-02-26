package org.jboss.jbossts.txbridge.tests.outbound.client;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
@WebService(name = "TestServiceAT", targetNamespace = "http://jboss.org/jbossts/txbridge/tests/outbound")
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