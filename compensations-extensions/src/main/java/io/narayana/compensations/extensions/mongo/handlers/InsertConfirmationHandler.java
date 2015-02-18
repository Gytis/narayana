package io.narayana.compensations.extensions.mongo.handlers;

import org.jboss.narayana.compensations.api.ConfirmationHandler;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class InsertConfirmationHandler implements ConfirmationHandler {

    @Override
    public void confirm() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return String.format("<%s>", getClass().getSimpleName());
    }

}
