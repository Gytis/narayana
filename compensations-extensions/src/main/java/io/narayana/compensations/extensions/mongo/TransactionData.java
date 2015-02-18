package io.narayana.compensations.extensions.mongo;

import org.bson.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO state might be better put in a separate class implementing specific interface
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TransactionData implements Serializable {

    private String transactionId;

    private String originalState;

    private String newState;

    private Date timestamp;

    public TransactionData(final String transactionId, final String originalState, final String newState) {
        this.transactionId = transactionId;
        this.originalState = originalState;
        this.newState = newState;
        this.timestamp = new Date();
    }

    public TransactionData(final String transactionId, final String originalState, final String newState,
            final Date timestamp) {

        this.transactionId = transactionId;
        this.originalState = originalState;
        this.newState = newState;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getOriginalState() {
        return originalState;
    }

    public String getNewState() {
        return newState;
    }

    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    @Override
    public String toString() {
        final String timestampString;
        if (timestamp != null) {
            timestampString = String.valueOf(timestamp.getTime());
        } else {
            timestampString = null;
        }

        return String.format("<%s: transactionId=%s, originalState=%s, newState=%s, timestamp=%s>",
                getClass().getSimpleName(), transactionId, originalState, newState, timestampString);
    }

    public Document toDocument() {
        final Document document = new Document();
        document.put("transactionId", transactionId);
        document.put("originalState", originalState);
        document.put("newState", newState);
        document.put("timestamp", timestamp);

        return document;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof TransactionData)) {
            return false;
        }

        final TransactionData transactionData = (TransactionData) o;

        boolean areEquals = transactionId.equals(transactionData.transactionId)
                && timestamp.equals(transactionData.timestamp);

        if (originalState == null) {
            areEquals &= transactionData.originalState == null;
        } else {
            areEquals &= originalState.equals(transactionData.originalState);
        }

        if (newState == null) {
            areEquals &= transactionData.newState == null;
        } else {
            areEquals &= newState.equals(transactionData.newState);
        }

        return areEquals;
    }

    public static TransactionData valueOf(String s) {
        s = s.substring(s.indexOf("=") + 1);
        String transactionId = s.substring(0, s.indexOf(","));
        transactionId = ("null".equals(transactionId) ? null : transactionId);

        s = s.substring(s.indexOf("=") + 1);
        String originalState = s.substring(0, s.indexOf(","));
        originalState = ("null".equals(originalState) ? null : originalState);

        s = s.substring(s.indexOf("=") + 1);
        String newState = s.substring(0, s.indexOf(","));
        newState = ("null".equals(newState) ? null : newState);

        s = s.substring(s.indexOf("=") + 1);
        String timestampString = s.substring(0, s.indexOf(">"));
        long timestamp = ("null".equals(timestampString) ? 0 : Long.valueOf(timestampString));

        return new TransactionData(transactionId, originalState, newState, new Date(timestamp));
    }

}
