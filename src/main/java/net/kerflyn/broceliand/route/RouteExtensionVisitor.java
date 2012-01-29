package net.kerflyn.broceliand.route;

import com.google.inject.spi.BindingTargetVisitor;

public interface RouteExtensionVisitor<T, V> extends BindingTargetVisitor<T, V> {

    V visit(RouteBinding routeBinding);

}
