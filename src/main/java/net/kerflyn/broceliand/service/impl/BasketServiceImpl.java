package net.kerflyn.broceliand.service.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.Invoice;
import net.kerflyn.broceliand.model.SellerPrice;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.BasketElementRepository;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.BookService;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import java.util.List;

public class BasketServiceImpl implements BasketService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BasketServiceImpl.class);

    private BasketElementRepository basketElementRepository;

    private BookService bookService;

    @Inject
    public BasketServiceImpl(BasketElementRepository basketElementRepository, BookService bookService) {
        this.basketElementRepository = basketElementRepository;
        this.bookService = bookService;
    }

    @Override
    @Transactional
    public List<BasketElement> findByUser(User user) {
        return basketElementRepository.findByUser(user);
    }

    @Override
    @Transactional
    public long countByUser(User user) {
        return basketElementRepository.countByUser(user);
    }

    @Override
    @Transactional
    public Invoice getCurrentInvoiceFor(User user) {
        Iterable<BasketElement> basketElements = findByUser(user);
        return new Invoice(basketElements);
    }

    @Override
    @Transactional
    public void addBookById(User user, Long bookId) {
        Book book = bookService.findById(bookId);
        BasketElement element;

        try {
            element = basketElementRepository.findByUserAndBook(user, book);
            element.setQuantity(element.getQuantity() + 1);
        } catch (NoResultException e) {
            SellerPrice sellerPrice = book.getLowestSellerPrice();

            element = new BasketElement();
            element.setOwner(user);
            element.setBook(book);
            element.setSeller(sellerPrice.getSeller());
            element.setQuantity(1);

            basketElementRepository.save(element);
        }

    }

    @Override
    @Transactional
    public void deleteBookById(User user, Long bookId) {
        Book book = bookService.findById(bookId);
        BasketElement element = basketElementRepository.findByUserAndBook(user, book);
        basketElementRepository.delete(element);
    }
}
