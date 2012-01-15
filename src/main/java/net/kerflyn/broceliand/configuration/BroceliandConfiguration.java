package net.kerflyn.broceliand.configuration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.kerflyn.broceliand.util.persist.PersistModule;

public class BroceliandConfiguration {

    public static Injector newGuiceInjector() {
        Injector injector = Guice.createInjector(
                new PersistModule("hsqldb-pu"),
                new ControllerModule(),
                new ServiceModule(),
                new RepositoryModule());
        return injector;
    }

}
