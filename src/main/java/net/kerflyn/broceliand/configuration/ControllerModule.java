package net.kerflyn.broceliand.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import net.kerflyn.broceliand.controller.BookController;
import net.kerflyn.broceliand.controller.IndexController;
import net.kerflyn.broceliand.controller.ResourceController;
import net.kerflyn.broceliand.controller.SellerController;
import net.kerflyn.broceliand.controller.UserController;

import static com.google.inject.name.Names.named;

public class ControllerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Key.get(Object.class, named("resources"))).to(ResourceController.class);
        bind(Key.get(Object.class, named("index"))).to(IndexController.class);
        bind(Key.get(Object.class, named("book"))).to(BookController.class);
        bind(Key.get(Object.class, named("user"))).to(UserController.class);
        bind(Key.get(Object.class, named("seller"))).to(SellerController.class);
    }

}
