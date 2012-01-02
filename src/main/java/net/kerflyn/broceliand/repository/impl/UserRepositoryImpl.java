package net.kerflyn.broceliand.repository.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        return entityManager.createQuery("select u from User u").getResultList();
    }

    @Override
    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    @Transactional
    public User findByLogin(String login) {
        final Query query = entityManager.createQuery("select u from User u where u.login = :login");
        query.setParameter("login", login);
        return (User) query.getSingleResult();
    }
}
