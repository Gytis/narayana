package org.oasis_open.docs.ws_tx.wsba._2006._06;

import com.arjuna.webservices11.wsba.BusinessActivityConstants;
import org.oasis_open.docs.ws_tx.wsba._2006._06.NotificationType;
import org.oasis_open.docs.ws_tx.wsba._2006._06.ObjectFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@WebService(targetNamespace = BusinessActivityConstants.WSBA_NOT_STANDARD_NAMESPACE)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
        ObjectFactory.class
})
public interface BusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType {

    @WebMethod(operationName = "CompletedOperation", action = "http://docs.oasis-open.org/ws-tx/wsba/2006/06/Completed")
    public void completedOperation(@WebParam(name = "Completed", targetNamespace = BusinessActivityConstants.WSBA_NAMESPACE, partName = "parameters")
            NotificationType parameters);

}
