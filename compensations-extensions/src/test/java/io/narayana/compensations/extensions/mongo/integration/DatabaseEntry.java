package io.narayana.compensations.extensions.mongo.integration;

import java.util.Map;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class DatabaseEntry implements Map.Entry<String, String> {

    private String key;

    private String value;

    public DatabaseEntry(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(final String value) {
        return this.value = value;
    }

    public boolean equals(final Object o) {
        if (o == null || !(o instanceof DatabaseEntry)) {
            return false;
        }

        final DatabaseEntry databaseEntry = (DatabaseEntry) o;
        boolean equals = true;

        if (key == null) {
            equals &= databaseEntry.key == null;
        } else {
            equals &= key.equals(databaseEntry.key);
        }

        if (value == null) {
            equals &= databaseEntry.value == null;
        } else {
            equals &= value.equals(databaseEntry.value);
        }

        return equals;
    }

}
