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
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.SellerService;
import net.kerflyn.broceliand.service.ShippingChargeStrategyService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.service.impl.BasketServiceImpl;
import net.kerflyn.broceliand.service.impl.BookServiceImpl;
import net.kerflyn.broceliand.service.impl.SellerServiceImpl;
import net.kerflyn.broceliand.service.impl.ShippingChargeStrategyServiceImpl;
import net.kerflyn.broceliand.service.impl.UserServiceImpl;
import net.kerflyn.broceliand.util.SessionManager;
import net.kerflyn.broceliand.util.SessionManagerImpl;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class);
        bind(BookService.class).to(BookServiceImpl.class);
        bind(BasketService.class).to(BasketServiceImpl.class);
        bind(SellerService.class).to(SellerServiceImpl.class);
        bind(ShippingChargeStrategyService.class).to(ShippingChargeStrategyServiceImpl.class);
        bind(SessionManager.class).to(SessionManagerImpl.class);
    }

}
