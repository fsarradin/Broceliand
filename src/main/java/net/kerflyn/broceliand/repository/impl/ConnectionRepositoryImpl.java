package net.kerflyn.broceliand.repository.impl;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Connection;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.ConnectionRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ConnectionRepositoryImpl implements ConnectionRepository {
    private EntityManager entityManager;

    @Inject
    public ConnectionRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Connection findByAddress(String hostAddress) {
        Query query = entityManager.createQuery("select cu from Connection cu where cu.hostAddress = :hostAddress");
        query.setParameter("hostAddress", hostAddress);
        return (Connection) query.getSingleResult();
    }

    @Override
    public void delete(Connection connection) {
        entityManager.remove(connection);
    }

    @Override
    public void save(Connection connection) {
        entityManager.persist(connection);
        entityManager.flush();
    }

    @Override
    public void deleteAllFor(User user) {
        Query query = entityManager.createQuery("delete Connection cu where cu.user = :user");
        query.setParameter("user", user);
        query.executeUpdate();
    }
}
