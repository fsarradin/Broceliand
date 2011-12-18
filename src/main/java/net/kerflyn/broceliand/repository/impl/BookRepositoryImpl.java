package net.kerflyn.broceliand.repository.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.repository.BookRepository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    @Inject
    EntityManager entityManager;

    @Override
    public List<Book> findAll() {
        Book book = new Book();
        book.setTitle("Pragmatic Programmer");
        book.setAuthor("Hunt, Thomas");
        return Arrays.asList(book);
    }

    @Transactional
    public void save(Book book) {
        entityManager.persist(book);
    }

}
