package com.arjuna.webservices11.wsba.sei;

import com.arjuna.webservices11.wsaddr.AddressingHelper;
import com.arjuna.webservices11.wsarj.ArjunaContext;
import com.arjuna.webservices11.wsba.BusinessActivityConstants;
import com.arjuna.webservices11.wsba.processors.ParticipantCompletionCoordinatorProcessor;
import org.oasis_open.docs.ws_tx.wsba._2006._06.BusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType;
import org.jboss.ws.api.addressing.MAP;
import org.oasis_open.docs.ws_tx.wsba._2006._06.NotificationType;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.Addressing;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@WebService(serviceName = BusinessActivityConstants.PARTICIPANT_COMPLETION_SYNCHRONOUS_COORDINATOR_SERVICE_NAME,
        portName = BusinessActivityConstants.PARTICIPANT_COMPLETION_SYNCHRONOUS_COORDINATOR_PORT_NAME,
        name = BusinessActivityConstants.PARTICIPANT_COMPLETION_SYNCHRONOUS_COORDINATOR_PORT_NAME,
        targetNamespace = BusinessActivityConstants.WSBA_NOT_STANDARD_NAMESPACE)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@HandlerChain(file = "/ws-t_handlers.xml")
@Addressing(required = true)
public class BusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortTypeImpl
        implements BusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType {

    @Resource
    private WebServiceContext webServiceCtx;

    @Override
    public void completedOperation(
            @WebParam(name = "Completed", targetNamespace = BusinessActivityConstants.WSBA_NAMESPACE, partName = "parameters")
            NotificationType parameters) {

        final MessageContext ctx = webServiceCtx.getMessageContext();
        final NotificationType completed = parameters;
        final MAP inboundMap = AddressingHelper.inboundMap(ctx);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        ParticipantCompletionCoordinatorProcessor.getProcessor().completed(completed, inboundMap, arjunaContext);
    }
}
