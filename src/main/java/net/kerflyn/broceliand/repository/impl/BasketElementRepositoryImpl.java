package net.kerflyn.broceliand.repository.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.BasketElementRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class BasketElementRepositoryImpl implements BasketElementRepository {

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<BasketElement> findByUser(User user) {
        final Query query = entityManager.createQuery("select be from BasketElement be where be.owner = :user");
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    @Transactional
    public long countByUser(User user) {
        final Query query = entityManager.createQuery("select sum(be.quantity) from BasketElement be where be.owner = :user");
        query.setParameter("user", user);
        final Long count = (Long) query.getSingleResult();
        return count == null ? 0L : count;
    }

    @Override
    @Transactional
    public BasketElement findByUserAndBook(User user, Book book) {
        final Query query = entityManager.createQuery("select be from BasketElement be where be.owner = :user and be.book = :book");
        query.setParameter("user", user);
        query.setParameter("book", book);
        return (BasketElement) query.getSingleResult();
    }

    @Override
    @Transactional
    public void save(BasketElement basketElement) {
        entityManager.persist(basketElement);
    }

}
