package net.kerflyn.broceliand.util.persist;

import com.google.inject.Inject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class TransactionInterceptor implements MethodInterceptor {

    @Inject
    private EntityManager entityManager;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        if (entityManager.getTransaction().isActive()) {
            return methodInvocation.proceed();
        }

        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Object result = null;
        try {
            result = methodInvocation.proceed();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return result;
    }
}
