package io.narayana.compensations.extensions.firsttry;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public interface CompensationData<T> {

    T getDataBeforeChange();

    T getDataAfterChange();

    void setDataBeforeChange(T dataBeforeChange);

    void setDataAfterChange(T dataAfterChange);

}
