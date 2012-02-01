package net.kerflyn.broceliand.test.configuration;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import net.kerflyn.broceliand.configuration.RepositoryModule;
import net.kerflyn.broceliand.configuration.ServiceModule;

public class BroceliandTestConfiguration {

    public static Injector newGuiceInjector() {
        Injector injector = Guice.createInjector(
                new JpaPersistModule("test-manager-pu"),
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
