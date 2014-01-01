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
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.route.PathName;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.SellerService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.util.SessionManager;
import net.kerflyn.broceliand.util.Users;
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
    private SessionManager sessionManager;

    @Inject
    public BookController(BookService bookService,
                          UserService userService,
                          SellerService sellerService,
                          BasketService basketService,
                          SessionManager sessionManager) {
        this.bookService = bookService;
        this.userService = userService;
        this.sellerService = sellerService;
        this.basketService = basketService;
        this.sessionManager = sessionManager;
    }

    public void index(Request request, Response response) throws IOException, LeaseException {
        renderAddOrModifyBook(request, response, "new", "Add book", null);
    }

    public void delete(Request request, Response response) throws IOException, LeaseException {
        bookService.deleteById(Long.valueOf(request.getParameter("book-id")));
        redirectTo(response, "/");
    }

    public void details(Request request, Response response) throws IOException, LeaseException {
        String bookIdStr = request.getParameter("book-id");

        Book book = null;

        if (bookIdStr != null) {
            book = bookService.findById(Long.valueOf(bookIdStr));
        }

        User user = Users.getConnectedUser(userService, request, sessionManager);

        if (user != null && user.isAdmin()) {
            renderAddOrModifyBook(request, response, "modify", "Modify book", book);
        } else {
            URL groupUrl = new File("template/details-book.stg").toURI().toURL();

            ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService, sessionManager);

            template.addAggr("data.{book, prices}",
                    new Object[]{book, bookService.findPricesFor(book)});

            response.getPrintStream().append(template.render());
        }
    }

    @PathName("new")
    public void createBook(Request request, Response response) throws IOException {
        Book book = new Book();
        book.setTitle(request.getParameter("title"));
        book.setAuthor(request.getParameter("author"));
        bookService.save(book);

        for (String name : request.getNames()) {
            if (name.startsWith(SELLER_PRICE_PREFIX)) {
                BigDecimal price = new BigDecimal(request.getPart(name).getContent());
                Long sellerId = Long.valueOf(name.substring(SELLER_PRICE_PREFIX.length()));

                bookService.setPrice(book, sellerService.findById(sellerId), price);
            }
        }

        redirectTo(response, "/");
    }

    public void modify(Request request, Response response) throws IOException {
        LOGGER.debug("form names: " + request.getNames());

        Book book = bookService.findById(Long.valueOf(request.getParameter("book-id")));
        book.setTitle(request.getParameter("title"));
        book.setAuthor(request.getParameter("author"));

        Set<Seller> processedSellers = newHashSet();

        for (String name : request.getNames()) {
            if (name.startsWith(SELLER_PRICE_PREFIX)) {
                BigDecimal price = new BigDecimal(request.getPart(name).getContent());

                Long sellerId = Long.valueOf(name.substring(SELLER_PRICE_PREFIX.length()));
                Seller seller = sellerService.findById(sellerId);
                processedSellers.add(seller);

                bookService.setPrice(book, seller, price);
            }
        }

        bookService.removeSellerPricesNotIn(processedSellers, book);

        redirectTo(response, "/");
    }

    private void renderAddOrModifyBook(Request request, Response response, String action, String actionName, Book book) throws LeaseException, IOException {
        URL groupUrl = new File("template/add-modify-book.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService, sessionManager);

        template.addAggr("data.{action, actionName, book, sellers, prices}",
                new Object[]{action, actionName, book, sellerService.findAll(), bookService.findPricesFor(book)});

        response.getPrintStream().append(template.render());
    }

}
