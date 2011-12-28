package net.kerflyn.broceliand.util;

import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.UserService;
import org.simpleframework.http.Request;
import org.simpleframework.http.session.Session;
import org.simpleframework.util.lease.LeaseException;

public class Users {

    public static final String CURRENT_USER_SESSION_KEY = "current-user";

    public static User getConnectedUser(UserService userService, Request request) throws LeaseException {
        User connectedUser = null;
        Session session = request.getSession(false);
        if (session != null && session.containsKey(CURRENT_USER_SESSION_KEY)) {
            connectedUser = userService.findByLogin((String) session.get(CURRENT_USER_SESSION_KEY));
        }
        return connectedUser;
    }

}
