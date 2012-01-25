package net.kerflyn.broceliand.service.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.ConnectedUser;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.ConnectedUserRepository;
import net.kerflyn.broceliand.repository.UserRepository;
import net.kerflyn.broceliand.service.UserService;
import org.joda.time.DateTime;

import javax.persistence.NoResultException;
import java.net.InetAddress;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ConnectedUserRepository connectedUserRepository;

    @Inject
    public UserServiceImpl(UserRepository userRepository, ConnectedUserRepository connectedUserRepository) {
        this.userRepository = userRepository;
        this.connectedUserRepository = connectedUserRepository;
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
        ConnectedUser connectedUser = null;
        try {
            connectedUser = connectedUserRepository.findByAddress(address.getHostAddress());
        } catch (NoResultException e) {
            return null;
        }
        User user = null;
        if (connectedUser.getExpirationDate().isAfterNow()) {
            user = connectedUser.getUser();
        } else {
            connectedUserRepository.delete(connectedUser);
        }
        return user;
    }

    @Override
    @Transactional
    public void saveConnection(User user, InetAddress address, DateTime expirationDate) {
        ConnectedUser connectedUser = new ConnectedUser();
        connectedUser.setUser(user);
        connectedUser.setExpirationDate(expirationDate);
        connectedUser.setHostAddress(address.getHostAddress());

        connectedUserRepository.save(connectedUser);
    }

    @Override
    @Transactional
    public void deleteAllConnectionsFor(User user) {
        connectedUserRepository.deleteAllFor(user);
    }

}
