package net.kerflyn.broceliand.controller;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.UserService;
import org.simpleframework.http.Form;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.session.Session;
import org.simpleframework.util.lease.LeaseException;
import org.stringtemplate.v4.ST;

import java.io.IOException;

import static net.kerflyn.broceliand.util.Templates.buildTemplate;
import static net.kerflyn.broceliand.util.Users.CURRENT_USER_SESSION_KEY;

public class LoginController {

    private UserService userService;

    @Inject
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    public void render(Request request, Response response) throws IOException {
        ST template = buildTemplate("public/login.html");
        response.getPrintStream().append(template.render());
    }

    @SuppressWarnings("unchecked")
    public void render(Request request, Response response, String action) throws IOException, LeaseException {
        if ("connect".equals(action)) {
            Form form = request.getForm();
            User user = userService.findByLogin(form.get("login"));
            if (user != null) {
                Session session = request.getSession(true);
                session.put(CURRENT_USER_SESSION_KEY, user.getLogin());
            }
        } else if ("logout".equals(action)) {
            Session session = request.getSession(false);
            if (session != null) {
                session.remove(CURRENT_USER_SESSION_KEY);
            }
        }

        response.setCode(Status.TEMPORARY_REDIRECT.getCode());
        response.set("Location", "/");
    }

}
