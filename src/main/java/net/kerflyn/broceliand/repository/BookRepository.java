package net.kerflyn.broceliand.repository;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.Book;

import javax.persistence.EntityManager;

public class BookRepository {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void save(Book book) {
        entityManager.persist(book);
    }

}
