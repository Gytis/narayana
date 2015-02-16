package io.narayana.compensations.extensions.mongo;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import java.util.Date;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class TransactionData<T> {

    private final String transactionId;

    private final OperationType operation;

    private final T originalDocument;

    private final T newDocument;

    private final Date timestamp;

    public TransactionData(final String transactionId, final OperationType operation,
            final T originalDocument, final T newDocument) {

        this.transactionId = transactionId;
        this.operation = operation;
        this.originalDocument = originalDocument;
        this.newDocument = newDocument;
        this.timestamp = new Date();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public OperationType getOperation() {
        return operation;
    }

    public T getOriginalDocument() {
        return originalDocument;
    }

    public T getNewDocument() {
        return newDocument;
    }

    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    public String toString() {
        return "<TransactionData: transactionId=" + transactionId + ", operation="+ operation + ", originalDocument="
                + originalDocument + ", newDocument=" + newDocument + ", timestamp=" + timestamp.getTime() + ">";
    }

    public Document toDocument() {
        final Document document = new Document();

        document.put("transactionId", transactionId);
        document.put("operation", operation.name());
        document.put("timestamp", timestamp.toString());

        if (originalDocument != null && originalDocument instanceof Document) {
            document.put("originalDocument", originalDocument.toString());
        }

        if (newDocument != null && newDocument instanceof Document) {
            document.put("newDocument", newDocument.toString());
        }

        return document;
    }

    public BsonDocument toBsonDocument() {
        final BsonDocument document = new BsonDocument();

        document.put("transactionId", new BsonString(transactionId));
        document.put("operation", new BsonString(operation.name()));
        document.put("timestamp", new BsonString(timestamp.toString()));

        if (originalDocument != null && originalDocument instanceof BsonDocument) {
            document.put("originalDocument", new BsonString(originalDocument.toString()));
        }

        if (newDocument != null && newDocument instanceof BsonDocument) {
            document.put("newDocument", new BsonString(newDocument.toString()));

        }

        return document;
    }

    public enum OperationType {
        INSERT, UPDATE, DELETE
    }
}
