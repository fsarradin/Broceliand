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
        User user = null;
        Session session = request.getSession(false);
        if (session != null && session.containsKey(CURRENT_USER_SESSION_KEY)) {
            user = userService.findByLogin((String) session.get(CURRENT_USER_SESSION_KEY));
        } else {
            InetAddress address = request.getClientAddress().getAddress();
            user = userService.checkConnectedAt(address);
            if (user != null) {
                login(user, request);
            }
        }
        return user;
    }

    public static void checkForAdminAccount(UserService userService) {
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
