package net.kerflyn.broceliand.service.impl;

import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.Invoice;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.BasketElementRepository;
import net.kerflyn.broceliand.service.BookService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BasketServiceImplWhenGettingInvoiceTest {

    private BasketServiceImpl basketService;
    private BasketElementRepository basketElementRepository;
    private BookService bookService;
    private Book book;
    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1L);

        book = new Book();
        book.setId(1L);
        book.setTitle("Toto");
        book.setPrice(new BigDecimal("40.00"));

        basketElementRepository = mock(BasketElementRepository.class);

        bookService = mock(BookService.class);
        when(bookService.findById(eq(1L))).thenReturn(book);

        basketService = new BasketServiceImpl(basketElementRepository, bookService);
    }

    @Test
    public void should_invoice_total_depend_on_basket_content() {
        BasketElement basketElement1 = new BasketElement();
        basketElement1.setOwner(user);
        basketElement1.setBook(book);
        basketElement1.setQuantity(5);

        when(basketService.findByUser(eq(user))).thenReturn(Arrays.asList(basketElement1));

        Invoice invoice = basketService.getCurrentInvoiceFor(user);
        assertThat(invoice.getTotal()).isEqualTo(new BigDecimal("200.00"));

        basketElement1.setQuantity(1);
        invoice = basketService.getCurrentInvoiceFor(user);
        assertThat(invoice.getTotal()).isEqualTo(new BigDecimal("40.00"));
    }
}
