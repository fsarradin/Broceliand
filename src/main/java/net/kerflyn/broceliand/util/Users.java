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

package net.kerflyn.broceliand.util;

import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.UserService;
import org.simpleframework.http.Request;
import org.simpleframework.http.session.Session;
import org.simpleframework.util.lease.LeaseException;

import javax.persistence.NoResultException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

public class Users {

    public static final String CURRENT_USER_SESSION_KEY = "current-user";

    public static User getConnectedUser(UserService userService, Request request) throws LeaseException {
        User user;
        Session session = request.getSession(false);

        if (session != null && session.containsKey(CURRENT_USER_SESSION_KEY)) {

            user = userService.findByLogin((String) session.get(CURRENT_USER_SESSION_KEY));

        } else {
            user = userService.checkConnectedAt(request.getClientAddress().getAddress());

            if (user != null) {
                login(user, request);
            }
        }

        return user;
    }

    /**
     * Check if there is an administrator account available. If not the account is automatically created.
     *
     * @param userService user service where the administrator account should exist
     */
    public static void autoCreateAdministratorAccount(UserService userService) {
        try{

            userService.findByLogin(User.ADMIN_LOGIN);

        } catch (NoResultException e) {
            User adminUser = new User();

            adminUser.setLogin(User.ADMIN_LOGIN);
            adminUser.setName(User.ADMIN_LOGIN);

            userService.save(adminUser);
        }
    }

    @SuppressWarnings("unchecked")
    public static void login(User user, Request request) throws LeaseException {
        Session session = request.getSession(true);

        session.getLease().renew(30, TimeUnit.DAYS);
        session.put(CURRENT_USER_SESSION_KEY, user.getLogin());
    }
}
