package org.jboss.jbossts.txbridge.outbound;

import javax.transaction.SystemException;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;

import com.arjuna.ats.jta.TransactionManager;
import com.arjuna.ats.jta.transaction.Transaction;

/**
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 *
 */
public abstract class AbstractJTAToWSATBridgeHandler<C extends MessageContext> implements Handler<C> {

    private final JaxWSTxOutboundBridgeHandler delegateHandler = new JaxWSTxOutboundBridgeHandler();

    @Override
    public boolean handleMessage(C context) {
        // TODO remove
        System.out.println("AbstractJTAToWSATBridgeHandler.handleMessage");

        if (isEnabled(context) && isTransaction()) {
            // TODO remove
            System.out.println("delegateHandler.handleMessage");

            return delegateHandler.handleMessage(context);
        }

        return true;
    }

    @Override
    public boolean handleFault(C context) {
        if (isEnabled(context) && isTransaction()) {
            return delegateHandler.handleFault(context);
        }

        return true;
    }

    @Override
    public void close(MessageContext context) {
        delegateHandler.close(context);
    }

    /**
     * TODO does not work in some cases
     *
     * @return
     */
    private boolean isTransaction() {
        Transaction transaction = null;

        try {
            transaction = (Transaction) TransactionManager.transactionManager().getTransaction();
        } catch (SystemException e) {
        }

        return transaction != null;
    }

    protected abstract boolean isEnabled(C context);

}