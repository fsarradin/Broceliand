package net.kerflyn.broceliand.repository.impl;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.ConnectedUser;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.ConnectedUserRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class ConnectedUserRepositoryImpl implements ConnectedUserRepository {
    private EntityManager entityManager;

    @Inject
    public ConnectedUserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ConnectedUser findByAddress(String hostAddress) {
        Query query = entityManager.createQuery("select cu from ConnectedUser cu where cu.hostAddress = :hostAddress");
        query.setParameter("hostAddress", hostAddress);
        return (ConnectedUser) query.getSingleResult();
    }

    @Override
    public void delete(ConnectedUser connectedUser) {
        entityManager.remove(connectedUser);
    }

    @Override
    public void save(ConnectedUser connectedUser) {
        entityManager.persist(connectedUser);
        entityManager.flush();
    }

    @Override
    public void deleteAllFor(User user) {
        Query query = entityManager.createQuery("delete ConnectedUser cu where cu.user = :user");
        query.setParameter("user", user);
        query.executeUpdate();
    }
}
