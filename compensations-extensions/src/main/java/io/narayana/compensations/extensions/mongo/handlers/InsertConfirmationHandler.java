package io.narayana.compensations.extensions.mongo.handlers;

import io.narayana.compensations.extensions.mongo.TransactionData;
import org.jboss.narayana.compensations.api.ConfirmationHandler;

import javax.inject.Inject;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class InsertConfirmationHandler implements ConfirmationHandler {

    @Inject
    private TransactionData transactionData;

    @Override
    public void confirm() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return String.format("<%s: transactionData=%s>", getClass().getSimpleName(), transactionData);
    }

}
