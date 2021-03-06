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

package net.kerflyn.broceliand.route;

import com.google.inject.Binder;

import java.util.Map;
import java.util.NoSuchElementException;

import static com.google.common.collect.Maps.newHashMap;

public abstract class RoutesConfiguration {

    private Map<String, RouteBuilder> routes = newHashMap();

    private Binder binder;

    public abstract void configure();

    protected RouteBuilder serve(String route) {
        RouteBuilder routeBuilder = new RouteBuilder(binder, route);

        routes.put(route, routeBuilder);

        return routeBuilder;
    }

    public Class<?> getControllerClassFor(String path) {
        RouteBuilder routeBuilder = routes.get(path);

        if (routeBuilder == null) {
            throw new NoSuchElementException("no controller bound to \"" + path + "\"");
        }

        return routeBuilder.getControllerClass();
    }

    public void setBinder(Binder binder) {
        this.binder = binder;
    }
}
