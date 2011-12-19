package net.kerflyn.broceliand.repository.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.repository.BookRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Book> findAll() {
        return entityManager.createQuery("select b from Book b").getResultList();
    }

    @Override
    @Transactional
    public void save(Book book) {
        entityManager.persist(book);
    }

}
