package net.kerflyn.broceliand.repository;

import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.User;

import java.util.List;

public interface BasketElementRepository {

    List<BasketElement> findByUser(User user);

    long countByUser(User user);

    BasketElement findByUserAndBook(User user, Book book);

    void save(BasketElement basketElement);
}
