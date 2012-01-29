package net.kerflyn.broceliand.guiceroute;

import com.google.inject.AbstractModule;
import com.google.inject.Key;

public abstract class RouteModule extends AbstractModule {

    private RouteModuleBuilder routeModuleBuilder;

    @Override
    protected void configure() {
        routeModuleBuilder = new RouteModuleBuilder(binder());
        configureRoutes();
    }

    protected abstract void configureRoutes();

    private RouteModuleBuilder getRouteBindingBuilder() {
        return routeModuleBuilder;
    }

    protected RouteBindingBuilder serveRegexp(String pattern) {
        return getRouteBindingBuilder().serveRegexp(pattern);
    }

    public static interface RouteBindingBuilder {

        void with(Class<?> controllerClass);

        void with(Key<?> key);

    }
}
