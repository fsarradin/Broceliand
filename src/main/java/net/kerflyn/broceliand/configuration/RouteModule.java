package net.kerflyn.broceliand.configuration;

import com.google.inject.AbstractModule;
import net.kerflyn.broceliand.controller.BookController;
import net.kerflyn.broceliand.controller.IndexController;
import net.kerflyn.broceliand.controller.ResourceController;
import net.kerflyn.broceliand.controller.SellerController;
import net.kerflyn.broceliand.controller.UserController;
import net.kerflyn.broceliand.route.Routes;
import net.kerflyn.broceliand.route.RoutesConfiguration;

public class RouteModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Routes.class).toInstance(Routes.create(binder(), new RoutesConfiguration() {
            @Override
            public void configure() {
                serve("/").with(IndexController.class);

                serve("/resources/").with(ResourceController.class);
                serve("/resources/css/").with(ResourceController.class);
                serve("/resources/js/").with(ResourceController.class);
                serve("/resources/img/").with(ResourceController.class);

                serve("/user/").with(UserController.class);
                serve("/book/").with(BookController.class);
                serve("/seller/").with(SellerController.class);
            }
        }));
    }

}
