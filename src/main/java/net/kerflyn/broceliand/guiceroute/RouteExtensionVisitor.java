package net.kerflyn.broceliand.guiceroute;

import com.google.inject.spi.BindingTargetVisitor;

public interface RouteExtensionVisitor<T, V> extends BindingTargetVisitor<T, V> {

    V visit(RouteBinding routeBinding);

}
