package io.narayana.compensations.extensions.firsttry;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class CompensatableOperationCDIExtension implements javax.enterprise.inject.spi.Extension {

    public void register(@Observes BeforeBeanDiscovery bbd, BeanManager bm) {

        //Current API
        bbd.addAnnotatedType(bm.createAnnotatedType(CompensatableOperationInterceptor.class));
    }

}
