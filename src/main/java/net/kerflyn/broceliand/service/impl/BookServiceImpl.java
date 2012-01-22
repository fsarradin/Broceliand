package net.kerflyn.broceliand.service.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.SellerPrice;
import net.kerflyn.broceliand.repository.BookRepository;
import net.kerflyn.broceliand.repository.SellerPriceRepository;
import net.kerflyn.broceliand.service.BookService;

import java.util.List;

public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private SellerPriceRepository sellerPriceRepository;

    @Inject
    public BookServiceImpl(BookRepository bookRepository, SellerPriceRepository sellerPriceRepository) {
        this.bookRepository = bookRepository;
        this.sellerPriceRepository = sellerPriceRepository;
    }

    @Override
    @Transactional
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<SellerPrice> findPricesFor(Book book) {
        return sellerPriceRepository.findAllByBook(book);
    }

    @Override
    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book findById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    @Transactional
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    @Transactional
    public void deleteById(Long bookId) {
        bookRepository.deleteById(bookId);
    }

}
