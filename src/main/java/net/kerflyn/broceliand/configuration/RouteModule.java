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
import net.kerflyn.broceliand.controller.BookController;
import net.kerflyn.broceliand.controller.IndexController;
import net.kerflyn.broceliand.controller.ResourceController;
import net.kerflyn.broceliand.controller.SellerController;
import net.kerflyn.broceliand.controller.UserController;
import net.kerflyn.broceliand.route.Routes;
import net.kerflyn.broceliand.route.RoutesConfiguration;

public class RouteModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Routes.class).toInstance(Routes.create(binder(), new RoutesConfiguration() {
            @Override
            public void configure() {
                serve("/").with(IndexController.class);

                serve("/resources/").with(ResourceController.class);
                serve("/resources/css/").with(ResourceController.class);
                serve("/resources/js/").with(ResourceController.class);
                serve("/resources/js/lib/").with(ResourceController.class);
                serve("/resources/img/").with(ResourceController.class);
                serve("/resources/template/").with(ResourceController.class);

                serve("/user/").with(UserController.class);
                serve("/book/").with(BookController.class);
                serve("/seller/").with(SellerController.class);
            }
        }));
    }

}
