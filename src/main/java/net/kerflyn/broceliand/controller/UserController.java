package net.kerflyn.broceliand.controller;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Invoice;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.util.HttpUtils;
import net.kerflyn.broceliand.util.Templates;
import net.kerflyn.broceliand.util.Users;
import org.simpleframework.http.Form;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.session.Session;
import org.simpleframework.util.lease.LeaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static net.kerflyn.broceliand.util.HttpUtils.redirectTo;
import static net.kerflyn.broceliand.util.Templates.buildTemplate;
import static net.kerflyn.broceliand.util.Templates.createTemplateWithUserAndBasket;
import static net.kerflyn.broceliand.util.Users.CURRENT_USER_SESSION_KEY;

public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private UserService userService;
    private BasketService basketService;

    @Inject
    public UserController(UserService userService, BasketService basketService) {
        this.userService = userService;
        this.basketService = basketService;
    }

    public void render(Request request, Response response) throws IOException {
        ST template = Templates.buildTemplate("public/create-user.html");
        response.getPrintStream().append(template.render());
    }

    public void render(Request request, Response response, String action) throws IOException, LeaseException {
        if ("new".equals(action)) {
            createUser(request, response);
        } else if ("login".equals(action)) {
//            ST template = buildTemplate("public/login.html");
            final URL groupUrl = new File("template/login.stg").toURI().toURL();
            ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);
            response.getPrintStream().append(template.render());
        } else if ("connect".equals(action)) {
            login(request, response);
        } else if ("logout".equals(action)) {
            logout(request, response);
        } else if ("basket-add".equals(action)) {
            Form form = request.getForm();
            Long bookId = Long.valueOf(form.get("book-id"));
            User currentUser = Users.getConnectedUser(userService, request);
            basketService.addBookById(currentUser, bookId);
            redirectTo(response, "/");
        } else if ("basket-delete".equals(action)) {
            Form form = request.getForm();
            Long bookId = Long.valueOf(form.get("book-id"));
            User currentUser = Users.getConnectedUser(userService, request);
            basketService.deleteBookById(currentUser, bookId);
            redirectTo(response, "/user/invoice");
        } else if ("invoice".equals(action)) {
            final URL groupUrl = new File("template/invoice.stg").toURI().toURL();
            ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);

            final User connectedUser = Users.getConnectedUser(userService, request);
            final Invoice invoice = basketService.getCurrentInvoiceFor(connectedUser);
            template.addAggr("data.{invoice}", new Object[] { invoice });

            response.getPrintStream().append(template.render());
        }
    }

    private void logout(Request request, Response response) throws LeaseException {
        Session session = request.getSession(false);
        if (session != null) {
            session.remove(CURRENT_USER_SESSION_KEY);
        }
        redirectTo(response, "/");
    }

    @SuppressWarnings("unchecked")
    private void login(Request request, Response response) throws IOException, LeaseException {
        Form form = request.getForm();
        User user = userService.findByLogin(form.get("login"));
        if (user != null) {
            Session session = request.getSession(true);
            session.getLease().renew(30, TimeUnit.DAYS);
            session.put(CURRENT_USER_SESSION_KEY, user.getLogin());
        }
        redirectTo(response, "/");
    }

    private void createUser(Request request, Response response) throws IOException {
        Form form = request.getForm();

        User user = new User();
        user.setName(form.get("name"));
        user.setLogin(form.get("login"));

        userService.save(user);

        redirectTo(response, "/");
    }

}
