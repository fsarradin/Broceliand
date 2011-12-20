package net.kerflyn.broceliand.controller;

import com.google.common.io.Files;
import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.service.BookService;
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

    @Inject
    public IndexController(BookService bookService) {
        this.bookService = bookService;
    }

    public void render(Request request, Response response) throws IOException {
        List<Book> books = bookService.findAll();
        LOGGER.debug("Number of books: " + books.size());

        String raw = Files.toString(new File("public/index.html"), UTF_8);
        ST template = new ST(raw, '$', '$');
        template.add("books", books);
        response.getPrintStream().append(template.render());
    }

}
