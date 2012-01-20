package net.kerflyn.broceliand.controller;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.util.Templates;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.util.lease.LeaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static net.kerflyn.broceliand.util.Templates.createTemplateWithUserAndBasket;

public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    private BookService bookService;

    private UserService userService;
    private BasketService basketService;

    @Inject
    public IndexController(BookService bookService, UserService userService, BasketService basketService) {
        this.bookService = bookService;
        this.userService = userService;
        this.basketService = basketService;
    }

    public void render(Request request, Response response) throws IOException, LeaseException {
//        final URL groupUrl = Resources.getResource("template/index.stg");
        final URL groupUrl = new File("template/index.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);

        List<Book> books = bookService.findAll();
        template.addAggr("data.{books}", new Object[] { books });

        response.getPrintStream().append(template.render());
    }

}
