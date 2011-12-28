package net.kerflyn.broceliand.controller;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.util.Users;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.util.lease.LeaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.io.IOException;
import java.util.List;

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

    public void render(Request request, Response response) throws IOException, LeaseException {
        User currentUser = Users.getConnectedUser(userService, request);

        List<Book> books = bookService.findAll();
        List<User> users = userService.findAll();

        ST template = buildTemplate("public/index.html");
        template.add("books", books);
        template.add("users", users);
        template.add("currentUser", currentUser);
        response.getPrintStream().append(template.render());
    }

}
