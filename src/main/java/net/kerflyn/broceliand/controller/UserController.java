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
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.route.PathName;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.util.Session;
import net.kerflyn.broceliand.util.SessionManager;
import net.kerflyn.broceliand.util.Users;
import org.joda.time.DateTime;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
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

    private SessionManager sessionManager;

    @Inject
    public UserController(UserService userService, BasketService basketService, SessionManager sessionManager) {
        this.userService = userService;
        this.basketService = basketService;
        this.sessionManager = sessionManager;
    }

    public void index(Request request, Response response) throws IOException, LeaseException {
        URL groupUrl = new File("template/create-user.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService, sessionManager);

        response.getPrintStream().append(template.render());
    }

    public void invoice(Request request, Response response) throws LeaseException, IOException {
        URL groupUrl = new File("template/invoice.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService, sessionManager);

        User connectedUser = Users.getConnectedUser(userService, request, sessionManager);

        template.addAggr("data.{invoice}", new Object[] {basketService.getCurrentInvoiceFor(connectedUser)});

        response.getPrintStream().append(template.render());
    }

    @PathName("basket-delete")
    public void basketDelete(Request request, Response response) throws IOException, LeaseException {
        User connectedUser = Users.getConnectedUser(userService, request, sessionManager);

        basketService.deleteBookById(connectedUser, Long.valueOf(request.getParameter("book-id")));

        redirectTo(response, "/user/invoice");
    }

    @PathName("basket-add")
    public void basketAdd(Request request, Response response) throws IOException, LeaseException {
        User connectedUser = Users.getConnectedUser(userService, request, sessionManager);

        basketService.addBookById(connectedUser, Long.valueOf(request.getParameter("book-id")));

        redirectTo(response, "/");
    }

    public void login(Request request, Response response) throws LeaseException, IOException {
        URL groupUrl = new File("template/login.stg").toURI().toURL();
        ST template = createTemplateWithUserAndBasket(request, groupUrl, userService, basketService, sessionManager);

        response.getPrintStream().append(template.render());
    }

    public void logout(Request request, Response response) throws LeaseException {
        Session session = sessionManager.getSession(request);

        if (session != null) {
            User user = Users.getConnectedUser(userService, request, sessionManager);
            if (user != null) {
                userService.deleteAllConnectionsFor(user);
            }
            session.remove(CURRENT_USER_SESSION_KEY);
            session.close();
        }

        redirectTo(response, "/");
    }

    @SuppressWarnings("unchecked")
    public void connect(Request request, Response response) throws IOException, LeaseException {
        User user = userService.findByLogin(request.getParameter("login"));

        if (user != null) {
            Users.login(user, request, sessionManager);

            DateTime expirationDate = new DateTime().plusDays(30);
            userService.saveConnection(user, request.getClientAddress().getAddress(), expirationDate);
        }

        redirectTo(response, "/");
    }

    @PathName("new")
    public void createUser(Request request, Response response) throws IOException {
        User user = new User();
        user.setName(request.getParameter("name"));
        user.setLogin(request.getParameter("login"));

        userService.save(user);

        redirectTo(response, "/");
    }

}
