package net.kerflyn.broceliand.controller;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.UserService;
import org.simpleframework.http.Form;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static com.google.common.base.Charsets.UTF_8;

public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void render(Request request, Response response) throws IOException {
        ST template = buildTemplate("public/create-user.html");
        response.getPrintStream().append(template.render());
    }

    private ST buildTemplate(String webpage) throws IOException {
        String raw = Files.toString(new File(webpage), UTF_8);
        return new ST(raw, '$', '$');
    }

    public void render(Request request, Response response, String action) throws IOException {
        if ("new".equals(action)) {
            Form form = request.getForm();

            User user = new User();
            user.setName(form.get("name"));
            user.setLogin(form.get("login"));

            userService.save(user);
        }

        response.setCode(Status.TEMPORARY_REDIRECT.getCode());
        response.set("Location", "/");
    }

}
