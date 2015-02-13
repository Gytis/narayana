package io.narayana.compensations.extensions.mongo.integration;

import org.jboss.narayana.compensations.api.ConfirmationHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TestConfirmationHandler implements ConfirmationHandler {

    public static AtomicInteger INVOCATIONS_COUNTER = new AtomicInteger();

    @Override
    public void confirm() {
        INVOCATIONS_COUNTER.incrementAndGet();
    }

}
