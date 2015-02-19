package io.narayana.compensations.extensions.mongo.integration;

import org.jboss.narayana.compensations.api.Compensatable;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class CompensatableService {

    public static AtomicInteger INVOCATIONS_COUNTER = new AtomicInteger();

    @Inject
    private DatabaseManager databaseManager;

    @Compensatable
    public void execute(final String key, final List<String> values) {
        INVOCATIONS_COUNTER.incrementAndGet();

        for (final String value : values) {
            databaseManager.insert(key, value);
        }
    }

}
