package io.narayana.compensations.extensions.mongo;

import org.jboss.javaee.mongodb.Mongo;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@Mongo
@MongoCompensatable
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 198)
public class MongoCompensationsInterceptor {

    @AroundInvoke
    public Object intercept(final InvocationContext ic) throws Exception {
        System.out.println("MongoCompensationsInterceptor: " + ic.getTarget() + "#" + ic.getMethod().getName());

        return ic.proceed();
    }

}
