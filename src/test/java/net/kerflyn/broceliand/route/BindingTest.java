package net.kerflyn.broceliand.route;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.fest.assertions.Assertions.assertThat;

public class BindingTest {

    private Routes routes;

    @Before
    public void setUp() throws Exception {
        routes = Routes.create(new RoutesConfiguration() {
            @Override
            public void configure() {
                serve("/user").with(MyUserController.class);
            }
        });
    }

    @Test
    public void should_get_controller_instance() throws Exception {
        Object controller = routes.getControllerFor("/user");

        assertThat(controller).isNotNull();
        assertThat(controller).isInstanceOf(MyUserController.class);
    }

    @Test
    public void should_get_controller_method() throws Exception {
        Method controllerMethod = routes.getControllerMethodFor("/user/index");

        assertThat(controllerMethod).isNotNull();
        assertThat(controllerMethod.getName()).isEqualTo("index");
    }

    @Test
    public void should_get_default_controller_method_without_name() throws Exception {
        Method controllerMethod = routes.getControllerMethodFor("/user/");

        assertThat(controllerMethod).isNotNull();
        assertThat(controllerMethod.getName()).isEqualTo("index");
    }

    public static class MyUserController {

        public void index() {}

    }
}
