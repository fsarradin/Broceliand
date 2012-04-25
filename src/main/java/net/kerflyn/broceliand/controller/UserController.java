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
import net.kerflyn.broceliand.model.Invoice;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.route.PathName;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.util.Users;
import org.joda.time.DateTime;
import org.simpleframework.http.Form;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.session.Session;
import org.simpleframework.util.lease.LeaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static net.kerflyn.broceliand.util.HttpUtils.redirectTo;
import static net.kerflyn.broceliand.util.Templates.createTemplateWithUserAndBasket;
import static net.kerflyn.broceliand.util.Users.CURRENT_USER_SESSION_KEY;

public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    private BasketService basketService;

    @Inject
    public UserController(UserService userService, BasketService basketService) {
        this.userService = userService;
        this.basketService = basketService;
    }

    public void index(Request request, Response response) throws IOException, LeaseException {
        URL groupUrl = new File("template/create-user.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);

        response.getPrintStream().append(template.render());
    }

    public void invoice(Request request, Response response) throws LeaseException, IOException {
        URL groupUrl = new File("template/invoice.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);

        User connectedUser = Users.getConnectedUser(userService, request);

        template.addAggr("data.{invoice}", new Object[] {basketService.getCurrentInvoiceFor(connectedUser)});

        response.getPrintStream().append(template.render());
    }

    @PathName("basket-delete")
    public void basketDelete(Request request, Response response) throws IOException, LeaseException {
        Form form = request.getForm();

        User connectedUser = Users.getConnectedUser(userService, request);

        basketService.deleteBookById(connectedUser, Long.valueOf(form.get("book-id")));

        redirectTo(response, "/user/invoice");
    }

    @PathName("basket-add")
    public void basketAdd(Request request, Response response) throws IOException, LeaseException {
        Form form = request.getForm();

        User connectedUser = Users.getConnectedUser(userService, request);

        basketService.addBookById(connectedUser, Long.valueOf(form.get("book-id")));

        redirectTo(response, "/");
    }

    public void login(Request request, Response response) throws LeaseException, IOException {
        URL groupUrl = new File("template/login.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService);

        response.getPrintStream().append(template.render());
    }

    public void logout(Request request, Response response) throws LeaseException {
        Session session = request.getSession(false);

        if (session != null) {
            User user = Users.getConnectedUser(userService, request);
            if (user != null) {
                userService.deleteAllConnectionsFor(user);
            }
            session.remove(CURRENT_USER_SESSION_KEY);
        }

        redirectTo(response, "/");
    }

    @SuppressWarnings("unchecked")
    public void connect(Request request, Response response) throws IOException, LeaseException {
        Form form = request.getForm();

        User user = userService.findByLogin(form.get("login"));

        if (user != null) {
            Users.login(user, request);

            DateTime expirationDate = new DateTime().plusDays(30);
            userService.saveConnection(user, request.getClientAddress().getAddress(), expirationDate);
        }

        redirectTo(response, "/");
    }

    @PathName("new")
    public void createUser(Request request, Response response) throws IOException {
        Form form = request.getForm();

        User user = new User();
        user.setName(form.get("name"));
        user.setLogin(form.get("login"));

        userService.save(user);

        redirectTo(response, "/");
    }

}
