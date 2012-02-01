package net.kerflyn.broceliand.route;

import com.google.inject.Binder;
import com.google.inject.Injector;
import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class Routes {

    private static final Logger LOGGER = LoggerFactory.getLogger(Routes.class);

    private RoutesConfiguration configuration;

    public Routes(RoutesConfiguration configuration) {
        this.configuration = configuration;
    }

    public static Routes create(Binder binder, RoutesConfiguration configuration) {
        configuration.setBinder(binder);
        configuration.configure();
        return new Routes(configuration);
    }

    public void handle(Request request, Response response, Injector injector) {
        Path path = request.getPath();
        LOGGER.info("handle \"{}\"", path.toString());

        Class<?> controllerClass = configuration.getControllerClassFor(path.getDirectory());
        LOGGER.info("controller class name: {}", controllerClass.getName());

        Method method = getMethodOf(controllerClass, path);
        LOGGER.info("controller method name: {}", method.getName());

        Object controller = injector.getInstance(controllerClass);

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
