package net.kerflyn.broceliand.route;

import com.google.inject.Key;
import com.google.inject.spi.ConstructorBinding;
import com.google.inject.spi.ConvertedConstantBinding;
import com.google.inject.spi.ExposedBinding;
import com.google.inject.spi.InstanceBinding;
import com.google.inject.spi.LinkedKeyBinding;
import com.google.inject.spi.ProviderBinding;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.spi.ProviderKeyBinding;
import com.google.inject.spi.UntargettedBinding;

public class RouteBinding {

    private String pattern;
    private Key<?> controllerKey;

    public RouteBinding(String pattern, Key<?> controllerKey) {
        this.pattern = pattern;
        this.controllerKey = controllerKey;
    }
}
