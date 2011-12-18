package net.kerflyn.broceliand.repository.impl;

import com.google.common.collect.Lists;
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
    @Transactional
    public List<Book> findAll() {
        // TODO have to remove this
        Book book = new Book();
        book.setTitle("Pragmatic Programmer");
        book.setAuthor("Hunt, Thomas");
        List<Book> result = Lists.newArrayList();
        result.add(book);

        List list = entityManager.createQuery("select b from Book b").getResultList();
        result.addAll(list);
        return result;
    }

    @Override
    @Transactional
    public void save(Book book) {
        entityManager.persist(book);
    }

}
