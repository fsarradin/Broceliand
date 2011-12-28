package net.kerflyn.broceliand.controller;

import com.google.common.io.Files;
import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.service.BookService;
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
import static net.kerflyn.broceliand.util.Templates.buildTemplate;

public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private BookService bookService;

    @Inject
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public void render(Request request, Response response) throws IOException {
        ST template = buildTemplate("public/create-book.html");
        response.getPrintStream().append(template.render());
    }

    public void render(Request request, Response response, String action) throws IOException {
        if ("new".equals(action)) {
            Form form = request.getForm();

            Book book = new Book();
            book.setTitle(form.get("title"));
            book.setAuthor(form.get("author"));
            book.setPrice(new BigDecimal(form.get("price")));

            bookService.save(book);
        }

        response.setCode(Status.TEMPORARY_REDIRECT.getCode());
        response.set("Location", "/");
    }

}
