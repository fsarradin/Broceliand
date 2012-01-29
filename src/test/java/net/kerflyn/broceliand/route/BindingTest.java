package net.kerflyn.broceliand.route;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.spi.DefaultBindingTargetVisitor;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class BindingTest {

    private Injector injector;

    @Test
    public void should() {
        injector = Guice.createInjector(new RouteModule() {
            @Override
            protected void configureRoutes() {
                serveRegexp(".*\\.html").with(MyController.class);
            }
        });

        boolean containsRouteBinding = false;

        for (Binding<?> binding: injector.getAllBindings().values()) {
            if (binding.getProvider().get() instanceof RouteDefinition) {
                containsRouteBinding = true;
            }
        }

        assertThat(containsRouteBinding).isTrue();

//        Object controller = injector.getInstance(Key.get(Object.class, Names.named("index.html")));
    }

    private static class MyController {
    }

}
