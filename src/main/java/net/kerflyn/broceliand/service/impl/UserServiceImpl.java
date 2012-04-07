package net.kerflyn.broceliand.service.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.Connection;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.ConnectionRepository;
import net.kerflyn.broceliand.repository.UserRepository;
import net.kerflyn.broceliand.service.UserService;
import org.joda.time.DateTime;

import javax.persistence.NoResultException;
import java.net.InetAddress;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ConnectionRepository connectionRepository;

    @Inject
    public UserServiceImpl(UserRepository userRepository, ConnectionRepository connectionRepository) {
        this.userRepository = userRepository;
        this.connectionRepository = connectionRepository;
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    @Transactional
    public User checkConnectedAt(InetAddress address) {
        Connection connection;
        try {
            connection = connectionRepository.findByAddress(address.getHostAddress());
        } catch (NoResultException e) {
            return null;
        }
        User user = null;
        if (connection.getExpirationDate().isAfterNow()) {
            user = connection.getUser();
        } else {
            connectionRepository.delete(connection);
        }
        return user;
    }

    @Override
    @Transactional
    public void saveConnection(User user, InetAddress address, DateTime expirationDate) {
        Connection connection = new Connection();
        connection.setUser(user);
        connection.setExpirationDate(expirationDate);
        connection.setHostAddress(address.getHostAddress());

        connectionRepository.save(connection);
    }

    @Override
    @Transactional
    public void deleteAllConnectionsFor(User user) {
        connectionRepository.deleteAllFor(user);
    }

}
