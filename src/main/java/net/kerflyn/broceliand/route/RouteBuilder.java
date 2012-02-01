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
