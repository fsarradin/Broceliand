package net.kerflyn.broceliand.guiceroute;

import com.google.inject.Key;

public class RouteBinding {

    private String pattern;
    private Key<?> controllerKey;

    public RouteBinding(String pattern, Key<?> controllerKey) {
        this.pattern = pattern;
        this.controllerKey = controllerKey;
    }
}
