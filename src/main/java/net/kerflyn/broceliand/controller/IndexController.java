package net.kerflyn.broceliand.controller;

import com.google.common.io.Files;
import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.UserService;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.session.Session;
import org.simpleframework.util.lease.LeaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static net.kerflyn.broceliand.util.Templates.buildTemplate;

public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    private BookService bookService;

    private UserService userService;

    @Inject
    public IndexController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    public void render(Request request, Response response) throws IOException {
        User currentUser = null;
        try {
            currentUser = getConnectedUser(request);
        } catch (LeaseException e) {
            throw new IOException(e);
        }

        List<Book> books = bookService.findAll();
        List<User> users = userService.findAll();

        ST template = buildTemplate("public/index.html");
        template.add("books", books);
        template.add("users", users);
        template.add("currentUser", currentUser);
        response.getPrintStream().append(template.render());
    }

    private User getConnectedUser(Request request) throws LeaseException {
        User connectedUser = null;
        Session session = request.getSession(false);
        if (session != null && session.containsKey("current-user")) {
            connectedUser = userService.findByLogin((String) session.get("current-user"));
        }
        return connectedUser;
    }

}
