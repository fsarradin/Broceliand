package net.kerflyn.broceliand;

import com.google.common.io.Files;
import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.service.BookService;
import org.simpleframework.http.Response;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

public class IndexController {

    private BookService bookService;

    @Inject
    public IndexController(BookService bookService) {
        this.bookService = bookService;
    }

    public void render(Response response) throws IOException {
        List<Book> books = bookService.findAll();

        String raw = Files.toString(new File("public/index.html"), UTF_8);
        ST template = new ST(raw, '$', '$');
        template.add("books", books);
        response.getPrintStream().append(template.render());
    }

}
