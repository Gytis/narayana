package com.arjuna.mw.wst11.client;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
public abstract class AbstractWSTXHandler implements SOAPHandler<SOAPMessageContext> {

    /**
     * Delegate handler does all the work in case XTS handler is enabled.
     */
    private final SOAPHandler<SOAPMessageContext> delegateHandler = new JaxWSHeaderContextProcessor();

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        // TODO remove
        System.out.println("AbstractWSTXHandler.handleMessage");
        if (isXTSEnabled(context)) {
            return delegateHandler.handleMessage(context);
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        if (isXTSEnabled(context)) {
            return delegateHandler.handleFault(context);
        }

        return true;
    }

    @Override
    public void close(MessageContext context) {
        delegateHandler.close(context);
    }

    @Override
    public Set<QName> getHeaders() {
        return delegateHandler.getHeaders();
    }

    protected abstract boolean isXTSEnabled(SOAPMessageContext context);

}