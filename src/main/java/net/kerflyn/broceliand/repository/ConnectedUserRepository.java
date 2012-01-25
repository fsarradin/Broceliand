package net.kerflyn.broceliand.repository;

import net.kerflyn.broceliand.model.ConnectedUser;
import net.kerflyn.broceliand.model.User;

public interface ConnectedUserRepository {

    ConnectedUser findByAddress(String hostAddress);

    void delete(ConnectedUser connectedUser);

    void save(ConnectedUser connectedUser);

    void deleteAllFor(User user);
}
