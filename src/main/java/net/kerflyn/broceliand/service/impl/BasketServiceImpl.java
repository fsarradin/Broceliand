package net.kerflyn.broceliand.service.impl;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.Invoice;
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
    public List<BasketElement> findByUser(User user) {
        return basketElementRepository.findByUser(user);
    }

    @Override
    public long countByUser(User user) {
        return basketElementRepository.countByUser(user);
    }

    @Override
    public Invoice getCurrentInvoiceFor(User user) {
        Iterable<BasketElement> basketElements = findByUser(user);
        return new Invoice(basketElements);
    }

    @Override
    public void addBookById(User user, Long bookId) {
        Book book = bookService.findById(bookId);
        BasketElement element;
        try {
            // FIXME: hack to refresh the book quantity for all dispatcher threads
            element = basketElementRepository.findByUserAndBook(user, book);
//            Integer quantity = element.getQuantity();
//            basketElementRepository.delete(element);
//
//            element = new BasketElement();
//            element.setOwner(user);
//            element.setBook(book);
            element.setQuantity(element.getQuantity() + 1);
            // end of hack
        } catch (NoResultException e) {
            element = new BasketElement();
            element.setOwner(user);
            element.setBook(book);
            element.setQuantity(1);
            basketElementRepository.save(element);
        }

    }

    @Override
    public void deleteBookById(User user, Long bookId) {
        Book book = bookService.findById(bookId);
        BasketElement element = basketElementRepository.findByUserAndBook(user, book);
        basketElementRepository.delete(element);
    }
}
