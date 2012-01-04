package net.kerflyn.broceliand.controller;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.util.Templates;
import net.kerflyn.broceliand.util.Users;
import org.simpleframework.http.Form;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.session.Session;
import org.simpleframework.util.lease.LeaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.io.IOException;

import static net.kerflyn.broceliand.util.Templates.buildTemplate;
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
            ST template = buildTemplate("public/login.html");
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
        }
    }

    private void logout(Request request, Response response) throws LeaseException {
        Session session = request.getSession(false);
        if (session != null) {
            session.remove(CURRENT_USER_SESSION_KEY);
        }
        redirectTo(response, "/");
    }

    private void redirectTo(Response response, String url) {
        response.setCode(Status.TEMPORARY_REDIRECT.getCode());
        response.set("Location", url);
    }

    @SuppressWarnings("unchecked")
    private void login(Request request, Response response) throws IOException, LeaseException {
        Form form = request.getForm();
        User user = userService.findByLogin(form.get("login"));
        if (user != null) {
            Session session = request.getSession(true);
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
