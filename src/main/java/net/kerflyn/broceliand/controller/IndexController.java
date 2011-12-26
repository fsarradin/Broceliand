package net.kerflyn.broceliand.controller;

import com.google.common.io.Files;
import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.UserService;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

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
        List<Book> books = bookService.findAll();
        List<User> users = userService.findAll();

        String raw = Files.toString(new File("public/index.html"), UTF_8);
        ST template = new ST(raw, '$', '$');
        template.add("books", books);
        template.add("users", users);
        response.getPrintStream().append(template.render());
    }

}
