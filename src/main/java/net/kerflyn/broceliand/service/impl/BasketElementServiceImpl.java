package net.kerflyn.broceliand.service.impl;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.BasketElementRepository;
import net.kerflyn.broceliand.service.BasketElementService;
import net.kerflyn.broceliand.service.BookService;

import javax.persistence.NoResultException;
import java.util.List;

public class BasketElementServiceImpl implements BasketElementService {

    private BasketElementRepository basketElementRepository;

    private BookService bookService;

    @Inject
    public BasketElementServiceImpl(BasketElementRepository basketElementRepository, BookService bookService) {
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
    public void addBookById(User user, Long bookId) {
        Book book = bookService.findById(bookId);
        BasketElement element;
        try {
            element = basketElementRepository.findByUserAndBook(user, book);
            element.setQuantity(element.getQuantity() + 1);
            basketElementRepository.update(element);
        } catch (NoResultException e) {
            element = new BasketElement();
            element.setOwner(user);
            element.setBook(book);
            element.setQuantity(1);
            basketElementRepository.save(element);
        }
    }
}
