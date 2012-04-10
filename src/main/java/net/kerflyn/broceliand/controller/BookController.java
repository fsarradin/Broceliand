/*
 * Copyright 2012 François Sarradin <fsarradin AT gmail DOT com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.kerflyn.broceliand.controller;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.SellerPrice;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.route.PathName;
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

    public void delete(Request request, Response response) throws IOException, LeaseException {
        Form form = request.getForm();
        Long bookId = Long.valueOf(form.get("book-id"));
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

        final User user = Users.getConnectedUser(userService, request);
        if (user != null && user.isAdmin()) {
            renderAddOrModifyBook(request, response, "modify", "Modify book", book);
        } else {
            URL groupUrl = new File("template/details-book.stg").toURI().toURL();
            ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);

            List<SellerPrice> prices = bookService.findPricesFor(book);

            template.addAggr("data.{book, prices}", new Object[]{book, prices});

            response.getPrintStream().append(template.render());
        }
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
        URL groupUrl = new File("template/add-modify-book.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);

        List<SellerPrice> prices = bookService.findPricesFor(book);
        List<Seller> sellers = sellerService.findAll();

        template.addAggr("data.{action, actionName, book, sellers, prices}",
                new Object[]{action, actionName, book, sellers, prices});

        response.getPrintStream().append(template.render());
    }

}
