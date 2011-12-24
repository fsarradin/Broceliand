package net.kerflyn.broceliand.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import net.kerflyn.broceliand.controller.ImageController;
import net.kerflyn.broceliand.controller.IndexController;
import net.kerflyn.broceliand.controller.BookController;
import net.kerflyn.broceliand.repository.BookRepository;
import net.kerflyn.broceliand.repository.impl.BookRepositoryImpl;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.impl.BookServiceImpl;

import static com.google.inject.name.Names.named;

public class BroceliandConfiguration {

    public static Injector newGuiceInjector() {
        Injector injector = Guice.createInjector(new JpaPersistModule("hsqldb-pu"),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(BookRepository.class).to(BookRepositoryImpl.class);
                        bind(BookService.class).to(BookServiceImpl.class);

                        bind(Key.get(Object.class, named("index"))).to(IndexController.class);
                        bind(Key.get(Object.class, named("img"))).to(ImageController.class);
                        bind(Key.get(Object.class, named("book"))).to(BookController.class);
                    }
                });
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
