package org.jboss.jbossts.txbridge.outbound;

import javax.xml.ws.BindingProvider;

import org.jboss.ws.api.configuration.AbstractClientFeature;

import com.arjuna.mw.wst11.client.WSTXFeature;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
public final class JTAToWSATBridgeFeature extends AbstractClientFeature {

    public static final String REQUEST_CONTEXT_KEY = "JTAToWSATBridge";

    public static final String ENABLED_VALUE = "true";

    public static final String DISABLED_VALUE = "false";

    protected boolean enabled = true;

    public JTAToWSATBridgeFeature() {
        super(JTAToWSATBridgeFeature.class.getName());
    }

    public JTAToWSATBridgeFeature(final boolean enabled) {
        this();
        this.enabled = enabled;
    }

    @Override
    protected void initializeBindingProvider(BindingProvider bp) {
        if (enabled) {
            bp.getRequestContext().put(REQUEST_CONTEXT_KEY, ENABLED_VALUE);
            bp.getRequestContext().put(WSTXFeature.REQUEST_CONTEXT_KEY, WSTXFeature.ENABLED_VALUE);
        } else {
            bp.getRequestContext().put(REQUEST_CONTEXT_KEY, DISABLED_VALUE);
        }
    }

}