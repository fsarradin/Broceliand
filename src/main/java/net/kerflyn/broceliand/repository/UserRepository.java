package net.kerflyn.broceliand.repository;

import net.kerflyn.broceliand.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    void save(User user);
}
