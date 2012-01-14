package net.kerflyn.broceliand.controller;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.SellerService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.util.Users;
import org.simpleframework.http.Form;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.util.lease.LeaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static net.kerflyn.broceliand.util.HttpUtils.redirectTo;
import static net.kerflyn.broceliand.util.Templates.buildTemplate;

public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private BookService bookService;
    private UserService userService;
    private SellerService sellerService;

    @Inject
    public BookController(BookService bookService, UserService userService, SellerService sellerService) {
        this.bookService = bookService;
        this.userService = userService;
        this.sellerService = sellerService;
    }

    public void render(Request request, Response response) throws IOException, LeaseException {
        renderBookPage(request, response, "new", "Add a new book", null);
    }

    public void render(Request request, Response response, String action) throws IOException, LeaseException {
        if ("new".equals(action)) {
            Form form = request.getForm();

            Book book = new Book();
            book.setTitle(form.get("title"));
            book.setAuthor(form.get("author"));
            book.setPrice(new BigDecimal(form.get("price")));

            bookService.save(book);
        } else if ("modify".equals(action)) {
            Form form = request.getForm();

            Long bookId = Long.valueOf(form.get("book-id"));
            Book book = bookService.findById(bookId);
            book.setTitle(form.get("title"));
            book.setAuthor(form.get("author"));
            book.setPrice(new BigDecimal(form.get("price")));
        } else if ("details".equals(action)) {
            Form form = request.getForm();
            final Long bookId = Long.valueOf(form.get("book-id"));
            Book book = bookService.findById(bookId);
            renderBookPage(request, response, "modify", "Modify a book", book);
        } else if ("delete".equals(action)) {
            Form form = request.getForm();
            final Long bookId = Long.valueOf(form.get("book-id"));
            bookService.deleteById(bookId);
        }

        redirectTo(response, "/");
    }

    private void renderBookPage(Request request, Response response, String action, String actionName, Book book) throws LeaseException, IOException {
        User currentUser = Users.getConnectedUser(userService, request);
        boolean isAdmin = Users.isAdmin(currentUser);
        List<Seller> sellers = sellerService.findAll();

        ST template = buildTemplate("public/add-modify-book.html");
        template.add("currentUser", currentUser);
        template.add("isAdmin", isAdmin);
        template.add("action", action);
        template.add("actionName", actionName);
        template.add("book", book);
        template.add("sellers", sellers);
        response.getPrintStream().append(template.render());
    }

}
