package net.kerflyn.broceliand.controller;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.BasketService;
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

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static net.kerflyn.broceliand.util.HttpUtils.redirectTo;
import static net.kerflyn.broceliand.util.Templates.buildTemplate;
import static net.kerflyn.broceliand.util.Templates.createTemplateWithUserAndBasket;

public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private BookService bookService;
    private UserService userService;
    private SellerService sellerService;
    private BasketService basketService;

    @Inject
    public BookController(BookService bookService, UserService userService, SellerService sellerService, BasketService basketService) {
        this.bookService = bookService;
        this.userService = userService;
        this.sellerService = sellerService;
        this.basketService = basketService;
    }

    public void render(Request request, Response response) throws IOException, LeaseException {
        renderBookPage(request, response, "new", "Add a new book", null);
    }

    public void render(Request request, Response response, String action) throws IOException, LeaseException {
        if ("new".equals(action)) {
            createBook(request, response);
        } else if ("modify".equals(action)) {
            modifyBook(request, response);
        } else if ("details".equals(action)) {
            showDetails(request, response);
        } else if ("delete".equals(action)) {
            Form form = request.getForm();
            final Long bookId = Long.valueOf(form.get("book-id"));
            bookService.deleteById(bookId);
            redirectTo(response, "/");
        }
    }

    private void createBook(Request request, Response response) throws IOException {
        Form form = request.getForm();

        Book book = new Book();
        book.setTitle(form.get("title"));
        book.setAuthor(form.get("author"));
        book.setPrice(new BigDecimal(form.get("price")));

        bookService.save(book);
        redirectTo(response, "/");
    }

    private void showDetails(Request request, Response response) throws IOException, LeaseException {
        Book book = null;

        Form form = request.getForm();
        String bookIdStr = form.get("book-id");
        if (bookIdStr != null) {
            Long bookId = Long.valueOf(bookIdStr);
            book = bookService.findById(bookId);
        }
        renderBookPage(request, response, "modify", "Modify a book", book);
    }

    private void modifyBook(Request request, Response response) throws IOException {
        Form form = request.getForm();

        Long bookId = Long.valueOf(form.get("book-id"));
        Book book = bookService.findById(bookId);
        book.setTitle(form.get("title"));
        book.setAuthor(form.get("author"));
        book.setPrice(new BigDecimal(form.get("price")));
        Set<Seller> sellers = book.getSellers();
        if (sellers == null) {
            sellers = newHashSet();
            book.setSellers(sellers);
        } else {
            sellers.clear();
        }
        for (String sellerId : form.getAll("sellers")) {
            sellers.add(sellerService.findById(Long.valueOf(sellerId)));
        }
        redirectTo(response, "/");
    }

    private void renderBookPage(Request request, Response response, String action, String actionName, Book book) throws LeaseException, IOException {
        final URL groupUrl = new File("template/add-modify-book.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);

        List<Seller> sellers = sellerService.findAll();

        List<SellerOption> sellerOptions = newArrayList();
        for (Seller seller : sellers) {
            boolean selected = (book != null) && book.getSellers().contains(seller);
            sellerOptions.add(new SellerOption(seller, selected));
        }

        template.addAggr("data.{action, actionName, book, sellerOptions}",
                new Object[] { action, actionName, book, sellerOptions });

        response.getPrintStream().append(template.render());
    }

    public static class SellerOption {
        private Seller seller;
        private boolean selected;

        public SellerOption(Seller seller, boolean selected) {
            this.seller = seller;
            this.selected = selected;
        }

        public Seller getSeller() {
            return seller;
        }

        public void setSeller(Seller seller) {
            this.seller = seller;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

}
