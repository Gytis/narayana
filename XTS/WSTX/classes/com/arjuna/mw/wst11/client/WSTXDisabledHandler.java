package com.arjuna.mw.wst11.client;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * This handler is used when XTS handler is disabled by default.
 * It handles messages only when SOAP message context parameter to enable XTS is passed.
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
public final class WSTXDisabledHandler implements SOAPHandler<SOAPMessageContext> {

    /**
     * SOAP message context key to contain handler's enabled/disabled state.
     */
    private static final String XTS_KEY = "XTS";

    /**
     * Value which has to be set to <code>XTS_KEY</code> parameter in order to enable XTS handler.
     */
    private static final String XTS_ENABLED_VALUE = "true";

    /**
     * Delegate handler does all the work in case XTS handler is enabled.
     */
    private final SOAPHandler<SOAPMessageContext> delegateHandler = new JaxWSHeaderContextProcessor();


    @Override
    public boolean handleMessage(SOAPMessageContext context) {
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

    private boolean isXTSEnabled(SOAPMessageContext context) {
        return XTS_ENABLED_VALUE.equals(context.get(XTS_KEY));
    }
}
