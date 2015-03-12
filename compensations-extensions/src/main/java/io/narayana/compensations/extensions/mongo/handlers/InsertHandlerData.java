package io.narayana.compensations.extensions.mongo.handlers;

//import io.narayana.compensations.extensions.mongo.CollectionInfo;
//import org.jboss.narayana.compensations.api.CompensationScoped;
//import org.jboss.weld.util.collections.ArraySet;
//
//import java.io.Serializable;
//import java.util.Set;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
//@CompensationScoped
//public class InsertHandlerData implements Serializable {
//
//    private String transactionId;
//
//    private Set<CollectionInfo> collectionInfoSet;
//
//    public InsertHandlerData() {
//        transactionId = "";
//        collectionInfoSet = new ArraySet<>();
//    }
//
//    public String getTransactionId() {
//        return transactionId;
//    }
//
//    public void setTransactionId(String transactionId) {
//        this.transactionId = transactionId;
//    }
//
//    public Set<CollectionInfo> getCollectionInfoSet() {
//        return new ArraySet<>(collectionInfoSet);
//    }
//
//    public void setCollectionInfoSet(Set<CollectionInfo> collectionInfoSet) {
//        this.collectionInfoSet = new ArraySet<>(collectionInfoSet);
//    }
//
//    public void addCollectionInfo(final CollectionInfo collectionInfo) {
//        collectionInfoSet.add(collectionInfo);
//    }
//
//    public void removeCollectionInfo(final CollectionInfo collectionInfo) {
//        collectionInfoSet.remove(collectionInfo);
//    }
//
//}
