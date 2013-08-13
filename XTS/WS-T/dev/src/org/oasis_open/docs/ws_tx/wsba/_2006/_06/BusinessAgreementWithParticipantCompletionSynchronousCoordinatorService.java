package org.oasis_open.docs.ws_tx.wsba._2006._06;

import com.arjuna.webservices11.wsba.BusinessActivityConstants;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@WebServiceClient(name = BusinessActivityConstants.PARTICIPANT_COMPLETION_SYNCHRONOUS_COORDINATOR_SERVICE_NAME,
        targetNamespace = BusinessActivityConstants.WSBA_NOT_STANDARD_NAMESPACE,
        wsdlLocation = "wsdl/wsba-participant-completion-synchronous-coordinator-binding.wsdl")
public class BusinessAgreementWithParticipantCompletionSynchronousCoordinatorService extends Service {

    private final static URL BUSINESSAGREEMENTWITHPARTICIPANTCOMPLETIONSYNCHRONOUSCOORDINATORSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(BusinessAgreementWithParticipantCompletionSynchronousCoordinatorService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = BusinessAgreementWithParticipantCompletionSynchronousCoordinatorService.class.getResource("");
            url = new URL(baseUrl, "wsdl/wsba-participant-completion-synchronous-coordinator-binding.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'wsdl/wsba-participant-completion-synchronous-coordinator-binding.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        BUSINESSAGREEMENTWITHPARTICIPANTCOMPLETIONSYNCHRONOUSCOORDINATORSERVICE_WSDL_LOCATION = url;
    }

    public BusinessAgreementWithParticipantCompletionSynchronousCoordinatorService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BusinessAgreementWithParticipantCompletionSynchronousCoordinatorService() {
        super(BUSINESSAGREEMENTWITHPARTICIPANTCOMPLETIONSYNCHRONOUSCOORDINATORSERVICE_WSDL_LOCATION,
                BusinessActivityConstants.PARTICIPANT_COMPLETION_SYNCHRONOUS_COORDINATOR_SERVICE_QNAME);
    }

    /**
     *
     * @return
     *     returns BusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType
     */
    @WebEndpoint(name = "BusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType")
    public BusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType getBusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType() {
        return super.getPort(BusinessActivityConstants.PARTICIPANT_COMPLETION_SYNCHRONOUS_COORDINATOR_PORT_QNAME,
                BusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType
     */
    @WebEndpoint(name = "BusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType")
    public BusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType getBusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType(WebServiceFeature... features) {
        return super.getPort(BusinessActivityConstants.PARTICIPANT_COMPLETION_SYNCHRONOUS_COORDINATOR_PORT_QNAME,
                BusinessAgreementWithParticipantCompletionSynchronousCoordinatorPortType.class, features);
    }

}
