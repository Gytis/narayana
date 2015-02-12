package io.narayana.compensations.extensions.firsttry;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@CompensatableOperation
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 197)
public class CompensatableOperationInterceptor {

    @AroundInvoke
    public Object intercept(final InvocationContext ic) throws Exception {
        Object result = null;

        try {
            System.out.println("!!! Invoking proceed");
            result = ic.proceed();
            System.out.println("!!! Came back from proceed");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return result;
    }

}
