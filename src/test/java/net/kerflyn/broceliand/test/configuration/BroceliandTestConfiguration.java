package net.kerflyn.broceliand.test.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import net.kerflyn.broceliand.configuration.ControllerModule;
import net.kerflyn.broceliand.configuration.RepositoryModule;
import net.kerflyn.broceliand.configuration.ServiceModule;
import net.kerflyn.broceliand.controller.BookController;
import net.kerflyn.broceliand.controller.IndexController;
import net.kerflyn.broceliand.controller.ResourceController;
import net.kerflyn.broceliand.controller.UserController;
import net.kerflyn.broceliand.repository.BasketElementRepository;
import net.kerflyn.broceliand.repository.BookRepository;
import net.kerflyn.broceliand.repository.UserRepository;
import net.kerflyn.broceliand.repository.impl.BasketElementRepositoryImpl;
import net.kerflyn.broceliand.repository.impl.BookRepositoryImpl;
import net.kerflyn.broceliand.repository.impl.UserRepositoryImpl;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.service.impl.BasketServiceImpl;
import net.kerflyn.broceliand.service.impl.BookServiceImpl;
import net.kerflyn.broceliand.service.impl.UserServiceImpl;
import net.kerflyn.broceliand.util.persist.PersistModule;

import static com.google.inject.name.Names.named;

public class BroceliandTestConfiguration {

    public static Injector newGuiceInjector() {
        Injector injector = Guice.createInjector(new PersistModule("test-manager-pu"),
                new ControllerModule(),
                new ServiceModule(),
                new RepositoryModule());
        return injector;
    }

}
