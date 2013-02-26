package com.arjuna.mw.wst11.client;

import static com.arjuna.mw.wst11.client.WSTXFeature.DISABLED_VALUE;
import static com.arjuna.mw.wst11.client.WSTXFeature.REQUEST_CONTEXT_KEY;

import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * This handler is used when XTS handler is enabled by default.
 * It handles every message unles SOAP message context parameter to disable XTS is passed.
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
public final class EnabledWSTXHandler extends AbstractWSTXHandler {

    @Override
    protected boolean isXTSEnabled(SOAPMessageContext context) {
        // TODO remove
        System.out.println("EnabledWSTXHandler.isXTSEnabled");
        System.out.println("context: " + context);
        return !DISABLED_VALUE.equals(context.get(REQUEST_CONTEXT_KEY));
    }

}