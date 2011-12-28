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
    public void render(Request request, Response response, String action) throws IOException {
        if ("connect".equals(action)) {
            Form form = request.getForm();
            User user = userService.findByLogin(form.get("login"));
            if (user != null) {
                Session session = null;
                try {
                    session = request.getSession(true);
                } catch (LeaseException e) {
                    throw new IOException(e);
                }
                session.put("current-user", user.getLogin());
            }
        }

        response.setCode(Status.TEMPORARY_REDIRECT.getCode());
        response.set("Location", "/");
    }

}
