package net.kerflyn.broceliand.route;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public abstract class RoutesConfiguration {

    private Map<String, RouteBuilder> routes = newHashMap();

    public abstract void configure();

    protected RouteBuilder serve(String route) {
        final RouteBuilder routeBuilder = new RouteBuilder(route);
        routes.put(route, routeBuilder);
        return routeBuilder;
    }

    public Class<?> getControllerClassFor(String route) {
        return routes.get(route).getControllerClass();
    }
}
