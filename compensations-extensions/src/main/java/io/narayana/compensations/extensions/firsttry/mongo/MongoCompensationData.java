package io.narayana.compensations.extensions.firsttry.mongo;

import io.narayana.compensations.extensions.firsttry.CompensationData;
import org.bson.BsonDocument;
import org.jboss.narayana.compensations.api.CompensationScoped;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@CompensationScoped
public class MongoCompensationData implements CompensationData<BsonDocument> {

    private BsonDocument dataBeforeChange;

    private BsonDocument dataAfterChange;

    public MongoCompensationData() {

    }

    public MongoCompensationData(BsonDocument dataBeforeChange, BsonDocument dataAfterChange) {
        this.dataBeforeChange = dataBeforeChange;
        this.dataAfterChange = dataAfterChange;
    }

    @Override
    public BsonDocument getDataBeforeChange() {
        return dataBeforeChange;
    }

    @Override
    public BsonDocument getDataAfterChange() {
        return dataAfterChange;
    }

    @Override
    public void setDataBeforeChange(BsonDocument dataBeforeChange) {
        this.dataBeforeChange = dataBeforeChange;
    }

    @Override
    public void setDataAfterChange(BsonDocument dataAfterChange) {
        this.dataAfterChange = dataAfterChange;
    }
}
