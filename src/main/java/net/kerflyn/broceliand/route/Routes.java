package net.kerflyn.broceliand.route;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class Routes {

    private RoutesConfiguration configuration;

    public Routes(RoutesConfiguration configuration) {
        this.configuration = configuration;
    }

    public static Routes create(RoutesConfiguration configuration) {
        configuration.configure();
        return new Routes(configuration);
    }

    public void handle(Request request, Response response) {
        Path path = request.getPath();
        Class<?> controllerClass = configuration.getControllerClassFor(path.getDirectory());
        Method method = getMethodOf(controllerClass, path);
        Object controller = instantiate(controllerClass);

        try {
            method.invoke(controller, request, response);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Object instantiate(Class<?> controllerClass) {
        Object controller = null;
        try {
            controller = controllerClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        return controller;
    }

    public static Method getMethodOf(Class<?> controllerClass, Path path) {
        String methodName = path.getName();

        if (methodName == null || "".equals(methodName)) {
            methodName = "index";
        }

        Method method = findMethodIn(controllerClass, methodName);

        if (method == null) {
            throw new NoSuchElementException("no method bind to \"" + path.getPath() + "\"");
        }

        return method;
    }

    public static Method findMethodIn(Class<?> controllerClass, String methodName) {
        Method method = null;

        Method[] methods = controllerClass.getMethods();

        for (Method m : methods) {
            PathName pathName = m.getAnnotation(PathName.class);
            if (pathName != null && Pattern.matches(pathName.value(), methodName)) {
                method = m;
            } else if (m.getName().equals(methodName)) {
                method = m;
            }
        }

        return method;
    }

}
