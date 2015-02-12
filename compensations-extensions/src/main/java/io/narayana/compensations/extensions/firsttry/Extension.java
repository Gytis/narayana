package io.narayana.compensations.extensions.firsttry;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public interface Extension<T> {

    T getInstance();

    T getInstance(T object);

}
