package net.kerflyn.broceliand.service.impl;

import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.BasketElementRepository;
import net.kerflyn.broceliand.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.persistence.NoResultException;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BasketElementServiceImplTest {

    private BasketElementServiceImpl basketElementService;
    private BasketElementRepository basketElementRepository;
    private BookService bookService;

    @Before
    public void setUp() {
        basketElementRepository = mock(BasketElementRepository.class);
        bookService = mock(BookService.class);

        basketElementService = new BasketElementServiceImpl(basketElementRepository, bookService);
    }

    @Test
    public void should_add_another_occurence_book_to_user() {
        Book book = new Book();
        book.setId(1L);
        when(bookService.findById(eq(1L))).thenReturn(book);

        User user = new User();
        user.setId(1L);

        BasketElement basketElement = new BasketElement();
        basketElement.setBook(book);
        basketElement.setOwner(user);
        basketElement.setQuantity(1);
        when(basketElementRepository.findByUserAndBook(eq(user), eq(book))).thenReturn(basketElement);

        basketElementService.addBookById(user, 1L);

        ArgumentCaptor<BasketElement> basketElementCaptor = ArgumentCaptor.forClass(BasketElement.class);
        verify(basketElementRepository).update(basketElementCaptor.capture());
        assertThat(basketElementCaptor.getValue().getQuantity()).isEqualTo(2);
    }

    @Test
    public void should_add_new_book_to_user() {
        Book book = new Book();
        book.setId(1L);
        when(bookService.findById(eq(1L))).thenReturn(book);

        User user = new User();
        user.setId(1L);

        when(basketElementRepository.findByUserAndBook(eq(user), eq(book))).thenThrow(new NoResultException());

        basketElementService.addBookById(user, 1L);

        ArgumentCaptor<BasketElement> basketElementCaptor = ArgumentCaptor.forClass(BasketElement.class);
        verify(basketElementRepository).save(basketElementCaptor.capture());
        assertThat(basketElementCaptor.getValue().getQuantity()).isEqualTo(1);
    }

}
