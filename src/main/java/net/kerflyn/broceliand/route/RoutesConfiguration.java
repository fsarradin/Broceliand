package net.kerflyn.broceliand.route;

import java.util.Map;
import java.util.NoSuchElementException;

import static com.google.common.collect.Maps.newHashMap;

public abstract class RoutesConfiguration {

    private Map<String, RouteBuilder> routes = newHashMap();

    public abstract void configure();

    protected RouteBuilder serve(String route) {
        final RouteBuilder routeBuilder = new RouteBuilder(route);
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
}
