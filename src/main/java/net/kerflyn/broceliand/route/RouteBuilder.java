package net.kerflyn.broceliand.route;

public class RouteBuilder {

    private String route;
    private Class<?> controllerClass;

    public RouteBuilder(String route) {
        this.route = route;
    }

    public void with(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }
}
