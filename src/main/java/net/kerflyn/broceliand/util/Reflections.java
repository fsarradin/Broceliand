package net.kerflyn.broceliand.util;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;

import java.lang.reflect.Method;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.find;
import static java.util.Arrays.asList;

public class Reflections {

    private Reflections() {
        throw new UnsupportedOperationException();
    }

    public static void invoke(Object target, final String methodName, List<Object> arguments) {
        checkNotNull(methodName);
        Method method = findMethod(target, methodName);
        Object[] convertedArguments = convertArguments(arguments, method);

        try {
            method.invoke(target, convertedArguments);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private static Object[] convertArguments(List<Object> arguments, Method method) {
        Object[] convertedArguments = new Object[arguments.size()];
        for (int i = 0; i < arguments.size(); i++) {
            Object convertedArgument = arguments.get(i);
            if (method.getParameterTypes()[i] == int.class) {
                convertedArgument = Integer.parseInt((String) convertedArgument);
            }
            convertedArguments[i] = convertedArgument;
        }
        return convertedArguments;
    }

    private static Method findMethod(Object target, String methodName) {
        List<Method> methods = asList(target.getClass().getMethods());
        return find(methods, methodHasName(methodName));
    }

    private static Predicate<Method> methodHasName(final String methodName) {
        return new Predicate<Method>() {
            @Override
            public boolean apply(Method method) {
                return methodName.equals(method.getName());
            }
        };
    }

}
