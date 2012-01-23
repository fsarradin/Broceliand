package net.kerflyn.broceliand.service;

import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.SellerPrice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface BookService {
    List<Book> findAll();

    List<SellerPrice> findPricesFor(Book book);

    void save(Book book);

    Book findById(Long bookId);

    void delete(Book book);

    void deleteById(Long bookId);

    void setPrice(Book book, Seller seller, BigDecimal price);

    void removeSellerPricesNotIn(Set<Seller> sellers, Book book);
}
