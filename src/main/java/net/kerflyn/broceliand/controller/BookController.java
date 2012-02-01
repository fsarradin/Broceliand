package net.kerflyn.broceliand.controller;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.SellerPrice;
import net.kerflyn.broceliand.route.PathName;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.SellerService;
import net.kerflyn.broceliand.service.UserService;
import org.simpleframework.http.Form;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.util.lease.LeaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static net.kerflyn.broceliand.util.HttpUtils.redirectTo;
import static net.kerflyn.broceliand.util.Templates.createTemplateWithUserAndBasket;

public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private static final String SELLER_PRICE_PREFIX = "seller-price-";

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

    public void index(Request request, Response response) throws IOException, LeaseException {
        renderAddOrModifyBook(request, response, "new", "Add book", null);
    }

    public void delete(Request request, Response response, String action) throws IOException, LeaseException {
        Form form = request.getForm();
        final Long bookId = Long.valueOf(form.get("book-id"));
        bookService.deleteById(bookId);
        redirectTo(response, "/");
    }

    public void details(Request request, Response response) throws IOException, LeaseException {
        Book book = null;

        Form form = request.getForm();
        String bookIdStr = form.get("book-id");
        if (bookIdStr != null) {
            Long bookId = Long.valueOf(bookIdStr);
            book = bookService.findById(bookId);
        }
        renderAddOrModifyBook(request, response, "modify", "Modify book", book);
    }

    @PathName("new")
    public void createBook(Request request, Response response) throws IOException {
        Form form = request.getForm();

        Book book = new Book();
        book.setTitle(form.get("title"));
        book.setAuthor(form.get("author"));
        bookService.save(book);

        for (Object obj : form.keySet()) {
            String key = (String) obj;
            if (key.startsWith(SELLER_PRICE_PREFIX)) {
                Long sellerId = Long.valueOf(key.substring(SELLER_PRICE_PREFIX.length()));
                Seller seller = sellerService.findById(sellerId);
                BigDecimal price = new BigDecimal(form.get(key));
                bookService.setPrice(book, seller, price);
            }
        }

        redirectTo(response, "/");
    }

    public void modify(Request request, Response response) throws IOException {
        Form form = request.getForm();
        LOGGER.debug("form names: " + form.keySet());

        Long bookId = Long.valueOf(form.get("book-id"));
        Book book = bookService.findById(bookId);
        book.setTitle(form.get("title"));
        book.setAuthor(form.get("author"));

        Set<Seller> processedSellers = newHashSet();
        for (Object obj : form.keySet()) {
            String key = (String) obj;
            if (key.startsWith(SELLER_PRICE_PREFIX)) {
                Long sellerId = Long.valueOf(key.substring(SELLER_PRICE_PREFIX.length()));
                Seller seller = sellerService.findById(sellerId);
                processedSellers.add(seller);
                BigDecimal price = new BigDecimal(form.get(key));
                bookService.setPrice(book, seller, price);
            }
        }

        bookService.removeSellerPricesNotIn(processedSellers, book);

        redirectTo(response, "/");
    }

    private void renderAddOrModifyBook(Request request, Response response, String action, String actionName, Book book) throws LeaseException, IOException {
        final URL groupUrl = new File("template/add-modify-book.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);

        List<SellerPrice> prices = bookService.findPricesFor(book);
        List<Seller> sellers = sellerService.findAll();

        template.addAggr("data.{action, actionName, book, sellers, prices}",
                new Object[]{action, actionName, book, sellers, prices});

        response.getPrintStream().append(template.render());
    }

}
