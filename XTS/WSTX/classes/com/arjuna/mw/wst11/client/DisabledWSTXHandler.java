package com.arjuna.mw.wst11.client;

import static com.arjuna.mw.wst11.client.WSTXFeature.ENABLED_VALUE;
import static com.arjuna.mw.wst11.client.WSTXFeature.REQUEST_CONTEXT_KEY;

import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * This handler is used when XTS handler is disabled by default. It handles messages only when SOAP message context parameter to
 * enable XTS is passed.
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
public final class DisabledWSTXHandler extends AbstractWSTXHandler {

    @Override
    protected boolean isXTSEnabled(SOAPMessageContext context) {
        // TODO remove
        System.out.println("DisabledWSTXHandler.isXTSEnabled");
        System.out.println("context: " + context);
        return ENABLED_VALUE.equals(context.get(REQUEST_CONTEXT_KEY));
    }

}