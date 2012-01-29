package net.kerflyn.broceliand.route;

import java.lang.reflect.Method;

public class Routes {

    private RoutesConfiguration configuration;

    public Routes(RoutesConfiguration configuration) {
        this.configuration = configuration;
    }

    public static Routes create(RoutesConfiguration configuration) {
        configuration.configure();
        return new Routes(configuration);
    }

    public Object getControllerFor(String route) {
        Class<?> controllerClass = configuration.getControllerClassFor(route);
        Object controller = null;
        try {
            controller = controllerClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return controller;
    }

    public Method getControllerMethodFor(String route) {
        Method method = null;
        int lastSeparator = route.lastIndexOf('/');
        String dirRoute = route.substring(0, lastSeparator);
        String resRoute = route.substring(lastSeparator + 1);
        if ("".equals(resRoute)) {
            resRoute = "index";
        }

        Class<?> controllerClass = configuration.getControllerClassFor(dirRoute);
        Method[] methods = controllerClass.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(resRoute)) {
                method = m;
            }
        }

        return method;
    }
}
