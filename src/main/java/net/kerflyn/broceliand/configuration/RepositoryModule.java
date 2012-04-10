/*
 * Copyright 2012 François Sarradin <fsarradin AT gmail DOT com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.kerflyn.broceliand.configuration;

import com.google.inject.AbstractModule;
import net.kerflyn.broceliand.repository.BasketElementRepository;
import net.kerflyn.broceliand.repository.BookRepository;
import net.kerflyn.broceliand.repository.ConnectionRepository;
import net.kerflyn.broceliand.repository.SellerPriceRepository;
import net.kerflyn.broceliand.repository.SellerRepository;
import net.kerflyn.broceliand.repository.ShippingChargeStrategyRepository;
import net.kerflyn.broceliand.repository.UserRepository;
import net.kerflyn.broceliand.repository.impl.BasketElementRepositoryImpl;
import net.kerflyn.broceliand.repository.impl.BookRepositoryImpl;
import net.kerflyn.broceliand.repository.impl.ConnectionRepositoryImpl;
import net.kerflyn.broceliand.repository.impl.SellerPriceRepositoryImpl;
import net.kerflyn.broceliand.repository.impl.SellerRepositoryImpl;
import net.kerflyn.broceliand.repository.impl.ShippingChargeStrategyRepositoryImpl;
import net.kerflyn.broceliand.repository.impl.UserRepositoryImpl;

public class RepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserRepository.class).to(UserRepositoryImpl.class);
        bind(BookRepository.class).to(BookRepositoryImpl.class);
        bind(BasketElementRepository.class).to(BasketElementRepositoryImpl.class);
        bind(SellerRepository.class).to(SellerRepositoryImpl.class);
        bind(SellerPriceRepository.class).to(SellerPriceRepositoryImpl.class);
        bind(ConnectionRepository.class).to(ConnectionRepositoryImpl.class);
        bind(ShippingChargeStrategyRepository.class).to(ShippingChargeStrategyRepositoryImpl.class);
    }

}
