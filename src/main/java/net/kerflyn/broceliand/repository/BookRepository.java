package net.kerflyn.broceliand.repository;

import net.kerflyn.broceliand.model.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();
    void save(Book book);
}