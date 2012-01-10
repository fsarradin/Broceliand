package net.kerflyn.broceliand.configuration;

import com.google.inject.AbstractModule;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.SellerService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.service.impl.BasketServiceImpl;
import net.kerflyn.broceliand.service.impl.BookServiceImpl;
import net.kerflyn.broceliand.service.impl.SellerServiceImpl;
import net.kerflyn.broceliand.service.impl.UserServiceImpl;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class);
        bind(BookService.class).to(BookServiceImpl.class);
        bind(BasketService.class).to(BasketServiceImpl.class);
        bind(SellerService.class).to(SellerServiceImpl.class);
    }

}
