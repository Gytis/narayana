package org.jboss.jbossts.txbridge.outbound;

import java.util.Iterator;

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import com.arjuna.ats.jta.TransactionManager;
import com.arjuna.mw.wst.common.SOAPUtil;
import com.arjuna.webservices.wsarj.ArjunaConstants;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
public abstract class AbstractJTAToWSATBridgeHandler<C extends MessageContext> implements Handler<C> {

    private final JaxWSTxOutboundBridgeHandler delegateHandler = new JaxWSTxOutboundBridgeHandler();

    @Override
    public boolean handleMessage(C context) {
        if (isEnabled(context) && isJTATransaction() && !isWSATContext(context)) {
            return delegateHandler.handleMessage(context);
        }

        return true;
    }

    @Override
    public boolean handleFault(C context) {
        if (isEnabled(context) && isJTATransaction() && !isWSATContext(context)) {
            return delegateHandler.handleFault(context);
        }

        return true;
    }

    @Override
    public void close(MessageContext context) {
        delegateHandler.close(context);
    }

    private boolean isJTATransaction() {
        boolean isJTATransaction = false;

        try {
            final Transaction transaction = TransactionManager.transactionManager().getTransaction();
            isJTATransaction = transaction != null;
        } catch (SystemException e) {
        }

        return isJTATransaction;
    }

    private boolean isWSATContext(final MessageContext context) {
        boolean isWSATContext = false;

        if (context instanceof SOAPMessageContext) {
            final SOAPMessageContext soapMessageContext = (SOAPMessageContext) context;
            final SOAPHeaderElement soapHeaderElement = getHeaderElement(soapMessageContext, ArjunaConstants.WSARJ_NAMESPACE,
                    ArjunaConstants.WSARJ_ELEMENT_INSTANCE_IDENTIFIER);

            isWSATContext = soapHeaderElement != null;
        }

        return isWSATContext;
    }

    private SOAPHeaderElement getHeaderElement(final SOAPMessageContext soapMessageContext, final String uri, final String name) {
        SOAPHeaderElement soapHeaderElement = null;

        try {
            final SOAPMessage soapMessage = soapMessageContext.getMessage();
            final SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
            final SOAPHeader soapHeader = soapEnvelope.getHeader();

            if (soapHeader != null) {
                soapHeaderElement = getHeaderElement(soapHeader, uri, name);
            }
        } catch (SOAPException e) {
        }

        return soapHeaderElement;
    }

    private SOAPHeaderElement getHeaderElement(final SOAPHeader soapHeader, final String uri, final String name)
            throws SOAPException {

        @SuppressWarnings("unchecked")
        final Iterator<SOAPHeaderElement> iterator = SOAPUtil.getChildElements(soapHeader);

        while (iterator.hasNext()) {
            final SOAPHeaderElement current = iterator.next();
            final Name currentName = current.getElementName();

            if ((currentName != null) && match(name, currentName.getLocalName()) && match(uri, currentName.getURI())) {
                return current;
            }
        }

        return null;
    }

    private boolean match(final Object lhs, final Object rhs) {
        if (lhs == null) {
            return rhs == null;
        }

        return lhs.equals(rhs);
    }

    protected abstract boolean isEnabled(C context);

}