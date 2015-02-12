package io.narayana.compensations.extensions.firsttry;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public interface OperationHandler<T> {

    T execute(T data);

}
