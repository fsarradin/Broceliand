package net.kerflyn.broceliand.route;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.internal.UniqueAnnotations;

public class RouteModuleBuilder {

    private Binder binder;

    public RouteModuleBuilder(Binder binder) {
        this.binder = binder;
    }

    public RouteModule.RouteBindingBuilder serveRegexp(String pattern) {
        return new RouteBindingBuilderImpl(pattern);
    }

    private class RouteBindingBuilderImpl implements RouteModule.RouteBindingBuilder {

        private String pattern;

        public RouteBindingBuilderImpl(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public void with(Class<?> controllerClass) {
            with(Key.get(controllerClass));
        }

        @Override
        public void with(Key<?> controllerKey) {
            binder.bind(Key.get(RouteDefinition.class, UniqueAnnotations.create()))
                .toProvider(new RouteDefinition(pattern, controllerKey));
        }
    }


}
