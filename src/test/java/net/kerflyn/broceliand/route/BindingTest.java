package net.kerflyn.broceliand.route;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.parse.PathParser;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BindingTest {

    private Routes routes;
    private Response response;
    private Request request;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        MyUserController.indexCalled = false;
        MyUserController.loginCalled = false;
        MyUserController.getUserCalled = false;

        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Routes.class).toInstance(Routes.create(binder(), new RoutesConfiguration() {
                    @Override
                    public void configure() {
                        serve("/user/").with(MyUserController.class);
                    }
                }));
            }
        });

        routes = injector.getInstance(Routes.class);

        response = mock(Response.class);
        request = mock(Request.class);
    }

    private void initRequestFor(String path) {
        Path requestPath = new PathParser(path);
        when(request.getMethod()).thenReturn("GET");
        when(request.getPath()).thenReturn(requestPath);
    }

    @Test
    public void should_call_the_method_index() {
        initRequestFor("/user/");

        assertThat(MyUserController.indexCalled).isFalse();
        assertThat(MyUserController.loginCalled).isFalse();
        assertThat(MyUserController.getUserCalled).isFalse();

        routes.handle(request, response, injector);

        assertThat(MyUserController.indexCalled).isTrue();
        assertThat(MyUserController.loginCalled).isFalse();
        assertThat(MyUserController.getUserCalled).isFalse();
    }

    @Test
    public void should_call_the_method_login() {
        initRequestFor("/user/login");

        assertThat(MyUserController.indexCalled).isFalse();
        assertThat(MyUserController.loginCalled).isFalse();
        assertThat(MyUserController.getUserCalled).isFalse();

        routes.handle(request, response, injector);

        assertThat(MyUserController.indexCalled).isFalse();
        assertThat(MyUserController.loginCalled).isTrue();
        assertThat(MyUserController.getUserCalled).isFalse();
    }

    @Test
    public void should_call_the_method_getUser() {
        initRequestFor("/user/42");

        assertThat(MyUserController.indexCalled).isFalse();
        assertThat(MyUserController.loginCalled).isFalse();
        assertThat(MyUserController.getUserCalled).isFalse();

        routes.handle(request, response, injector);

        assertThat(MyUserController.indexCalled).isFalse();
        assertThat(MyUserController.loginCalled).isFalse();
        assertThat(MyUserController.getUserCalled).isTrue();
    }

    public static class MyUserController {

        public static boolean indexCalled = false;
        public static boolean loginCalled = false;
        public static boolean getUserCalled = false;

        public void index(Request request, Response response) {
            indexCalled = true;
        }

        @PathName("login")
        public void doLogin(Request request, Response response) {
            loginCalled = true;
        }

        @PathName("[0-9]+")
        public void getUser(Request request, Response response) {
            getUserCalled = true;
        }

    }
}
