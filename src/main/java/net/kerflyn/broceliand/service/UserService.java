package net.kerflyn.broceliand.service;

import net.kerflyn.broceliand.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    void save(User user);

    User findByLogin(String login);
}
