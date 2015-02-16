package io.narayana.compensations.extensions.mongo.integration;

import org.jboss.narayana.compensations.api.Compensatable;
import org.jboss.narayana.compensations.api.CompensationManager;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class CompensatableService {

    public static AtomicInteger INVOCATIONS_COUNTER = new AtomicInteger();

    @Inject
    private DatabaseManager databaseManager;

    @Compensatable
    public void execute() {
        INVOCATIONS_COUNTER.incrementAndGet();
        databaseManager.insertDocument("test", "1");
        databaseManager.insertBsonDocument("test", "2");
    }

}
