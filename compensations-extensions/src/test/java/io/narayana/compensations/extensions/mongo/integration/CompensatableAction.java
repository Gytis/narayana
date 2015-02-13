package io.narayana.compensations.extensions.mongo.integration;

import org.jboss.narayana.compensations.api.TxCompensate;
import org.jboss.narayana.compensations.api.TxConfirm;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class CompensatableAction {

    @TxCompensate(TestCompensationHandler.class)
    @TxConfirm(TestConfirmationHandler.class)
    public void execute() {
        System.out.println("kuku");
    }

}
