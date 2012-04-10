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

public class RouteBuilder {

    private Binder binder;

    private String route;

    private Class<?> controllerClass;

    public RouteBuilder(Binder binder, String route) {
        this.binder = binder;
        this.route = route;
    }

    public void with(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
        binder.bind(this.controllerClass);
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public String getRoute() {
        return route;
    }
}
