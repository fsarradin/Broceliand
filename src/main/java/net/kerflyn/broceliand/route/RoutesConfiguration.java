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
        final RouteBuilder routeBuilder = new RouteBuilder(binder, route);
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
