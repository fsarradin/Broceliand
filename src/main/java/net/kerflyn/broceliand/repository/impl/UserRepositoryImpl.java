package net.kerflyn.broceliand.repository.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        return entityManager.createQuery("select b from User b").getResultList();
    }
}
