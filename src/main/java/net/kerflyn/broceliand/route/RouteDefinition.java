package net.kerflyn.broceliand.route;

import com.google.inject.Key;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.spi.ProviderWithExtensionVisitor;

public class RouteDefinition implements ProviderWithExtensionVisitor<RouteDefinition> {

    private String pattern;
    private Key<?> controllerKey;

    public RouteDefinition(String pattern, Key<?> controllerKey) {
        this.pattern = pattern;
        this.controllerKey = controllerKey;
    }

    @Override
    public RouteDefinition get() {
        return this;
    }

    @Override
    public <B, V> V acceptExtensionVisitor(BindingTargetVisitor<B, V> visitor, ProviderInstanceBinding<? extends B> binding) {
        if (visitor instanceof RouteExtensionVisitor) {
            return ((RouteExtensionVisitor<B, V>) visitor).visit(new RouteBinding(pattern, controllerKey));
        } else {
            return visitor.visit(binding);
        }
    }

}
