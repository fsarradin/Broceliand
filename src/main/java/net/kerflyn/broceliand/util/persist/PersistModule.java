package net.kerflyn.broceliand.util.persist;

import com.google.inject.AbstractModule;
import org.aopalliance.intercept.MethodInterceptor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

public class PersistModule extends AbstractModule {

    private String persistenceUnit;

    public PersistModule(String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }

    @Override
    protected void configure() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(persistenceUnit);
        EntityManager entityManager = factory.createEntityManager();

        bind(EntityManager.class).toInstance(entityManager);

        MethodInterceptor transactionInterceptor = new TransactionInterceptor();
        requestInjection(transactionInterceptor);
        bindInterceptor(annotatedWith(Transactional.class), any(), transactionInterceptor);
        bindInterceptor(any(), annotatedWith(Transactional.class), transactionInterceptor);
    }
}
