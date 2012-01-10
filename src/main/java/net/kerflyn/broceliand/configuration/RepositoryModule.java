package net.kerflyn.broceliand.configuration;

import com.google.inject.AbstractModule;
import net.kerflyn.broceliand.repository.BasketElementRepository;
import net.kerflyn.broceliand.repository.BookRepository;
import net.kerflyn.broceliand.repository.SellerRepository;
import net.kerflyn.broceliand.repository.UserRepository;
import net.kerflyn.broceliand.repository.impl.BasketElementRepositoryImpl;
import net.kerflyn.broceliand.repository.impl.BookRepositoryImpl;
import net.kerflyn.broceliand.repository.impl.SellerRepositoryImpl;
import net.kerflyn.broceliand.repository.impl.UserRepositoryImpl;

public class RepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserRepository.class).to(UserRepositoryImpl.class);
        bind(BookRepository.class).to(BookRepositoryImpl.class);
        bind(BasketElementRepository.class).to(BasketElementRepositoryImpl.class);
        bind(SellerRepository.class).to(SellerRepositoryImpl.class);
    }

}
