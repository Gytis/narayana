package io.narayana.compensations.extensions.mongo.integration;

import org.jboss.narayana.compensations.api.CompensationHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TestCompensationHandler implements CompensationHandler {

    public static AtomicInteger INVOCATIONS_COUNTER = new AtomicInteger();

    @Override
    public void compensate() {
        INVOCATIONS_COUNTER.incrementAndGet();
    }

}
