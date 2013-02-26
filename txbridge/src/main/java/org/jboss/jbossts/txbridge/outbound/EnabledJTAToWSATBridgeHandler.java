package org.jboss.jbossts.txbridge.outbound;

import static org.jboss.jbossts.txbridge.outbound.JTAToWSATBridgeFeature.DISABLED_VALUE;
import static org.jboss.jbossts.txbridge.outbound.JTAToWSATBridgeFeature.REQUEST_CONTEXT_KEY;

import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 * @param <C>
 */
public class EnabledJTAToWSATBridgeHandler<C extends MessageContext> extends AbstractJTAToWSATBridgeHandler<C> {

    @Override
    protected boolean isEnabled(C context) {
        // TODO remove
        System.out.println("EnabledJTAToWSATBridgeHandler.isEnabled");

        return !DISABLED_VALUE.equals(context.get(REQUEST_CONTEXT_KEY));
    }

}