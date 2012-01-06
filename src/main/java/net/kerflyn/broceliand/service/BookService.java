package net.kerflyn.broceliand.service;

import net.kerflyn.broceliand.model.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();

    void save(Book book);

    Book findById(Long bookId);

    void delete(Book book);

    void deleteById(Long bookId);
}
