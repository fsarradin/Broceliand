package net.kerflyn.broceliand.repository;

import net.kerflyn.broceliand.model.Connection;
import net.kerflyn.broceliand.model.User;

public interface ConnectionRepository {

    Connection findByAddress(String hostAddress);

    void delete(Connection connection);

    void save(Connection connection);

    void deleteAllFor(User user);
}
