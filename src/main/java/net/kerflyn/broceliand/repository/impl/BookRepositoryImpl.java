package net.kerflyn.broceliand.repository.impl;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.repository.BookRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    @Inject
    EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Book> findAll() {
        return entityManager.createQuery("select b from Book b").getResultList();
    }

    @Override
    public void save(Book book) {
        entityManager.persist(book);
    }

    @Override
    public Book findById(Long bookId) {
        final Query query = entityManager.createQuery("select b from Book b where b.id = :id");
        query.setParameter("id", bookId);
        return (Book) query.getSingleResult();
    }

    @Override
    public void delete(Book book) {
        entityManager.remove(book);
    }

    @Override
    public void deleteById(Long bookId) {
        final Query query = entityManager.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", bookId);
        query.executeUpdate();
    }

}
