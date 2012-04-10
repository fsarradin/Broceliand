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

package net.kerflyn.broceliand.test.configuration;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import net.kerflyn.broceliand.configuration.RepositoryModule;
import net.kerflyn.broceliand.configuration.RouteModule;
import net.kerflyn.broceliand.configuration.ServiceModule;

public class BroceliandTestConfiguration {

    public static Injector newGuiceInjector() {
        Injector injector = Guice.createInjector(
                new JpaPersistModule("test-manager-pu"),
                new RouteModule(),
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
