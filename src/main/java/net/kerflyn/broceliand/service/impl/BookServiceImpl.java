package net.kerflyn.broceliand.service.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.SellerPrice;
import net.kerflyn.broceliand.repository.BookRepository;
import net.kerflyn.broceliand.repository.SellerPriceRepository;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.SellerService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private SellerPriceRepository sellerPriceRepository;
    private SellerService sellerService;

    @Inject
    public BookServiceImpl(BookRepository bookRepository,
                           SellerPriceRepository sellerPriceRepository,
                           SellerService sellerService) {
        this.bookRepository = bookRepository;
        this.sellerPriceRepository = sellerPriceRepository;
        this.sellerService = sellerService;
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

    @Override
    public void setPrice(Book book, Seller seller, BigDecimal price) {
        SellerPrice sellerPrice = findSellerPriceBySeller(book, seller);
        if (sellerPrice == null) {
            sellerPrice = new SellerPrice();
            sellerPrice.setBook(book);
            sellerPrice.setSeller(seller);
            sellerPriceRepository.save(sellerPrice);
            book.getSellerPrices().add(sellerPrice);
        }
        sellerPrice.setPrice(price);
    }

    @Override
    public void removeSellerPricesNotIn(Set<Seller> sellers, Book book) {
        List<SellerPrice> sellerPrices = findPricesFor(book);
        for (SellerPrice sellerPrice: sellerPrices) {
            if (!sellers.contains(sellerPrice.getSeller())) {
                sellerPriceRepository.delete(sellerPrice);
            }
        }
    }

    private SellerPrice findSellerPriceBySeller(Book book, Seller seller) {
        for (SellerPrice sellerPrice: findPricesFor(book)) {
            if (seller.getId().equals(sellerPrice.getSeller().getId())) {
                return sellerPrice;
            }
        }

        return null;
    }

}
