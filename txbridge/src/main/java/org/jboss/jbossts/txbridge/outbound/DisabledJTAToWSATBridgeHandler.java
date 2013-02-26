package org.jboss.jbossts.txbridge.outbound;

import static org.jboss.jbossts.txbridge.outbound.JTAToWSATBridgeFeature.ENABLED_VALUE;
import static org.jboss.jbossts.txbridge.outbound.JTAToWSATBridgeFeature.REQUEST_CONTEXT_KEY;

import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 * @param <C>
 */
public class DisabledJTAToWSATBridgeHandler<C extends MessageContext> extends AbstractJTAToWSATBridgeHandler<C> {

    @Override
    protected boolean isEnabled(C context) {
        return ENABLED_VALUE.equals(context.get(REQUEST_CONTEXT_KEY));
    }

}