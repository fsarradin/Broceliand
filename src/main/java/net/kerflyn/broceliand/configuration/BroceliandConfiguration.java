package net.kerflyn.broceliand.configuration;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class BroceliandConfiguration {

    public static Injector newGuiceInjector() {
        Injector injector = Guice.createInjector(
                new JpaPersistModule("hsqldb-pu"),
                new RouteModule() ,
                new ServiceModule(),
                new RepositoryModule());
        injector.getInstance(PersistenceInitializer.class);
        return injector;
    }

    private static class PersistenceInitializer {
        @Inject
        public PersistenceInitializer(PersistService service) {
            service.start();
        }
    }
}
