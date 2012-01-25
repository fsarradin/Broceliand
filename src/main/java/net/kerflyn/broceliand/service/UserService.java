package net.kerflyn.broceliand.service;

import net.kerflyn.broceliand.model.User;
import org.joda.time.DateTime;

import java.net.InetAddress;
import java.util.List;

public interface UserService {
    List<User> findAll();

    void save(User user);

    User findByLogin(String login);

    User checkConnectedAt(InetAddress address);

    void saveConnection(User user, InetAddress address, DateTime date);

    void deleteAllConnectionsFor(User user);
}
