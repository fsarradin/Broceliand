package net.kerflyn.broceliand.service.impl;

import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.repository.BookRepository;
import net.kerflyn.broceliand.repository.SellerPriceRepository;
import net.kerflyn.broceliand.service.SellerService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BookServiceImplWhenDeleteTest {

    private BookRepository bookRepository;
    private BookServiceImpl bookService;
    private SellerPriceRepository sellerPriceRepository;
    private SellerService sellerService;

    @Before
    public void setUp() throws Exception {
        bookRepository = mock(BookRepository.class);
        sellerPriceRepository = mock(SellerPriceRepository.class);
        sellerService = mock(SellerService.class);
        bookService = new BookServiceImpl(bookRepository, sellerPriceRepository, sellerService);
    }

    @Test
    public void should_delete_book() {
        Book book = new Book();
        bookService.delete(book);

        verify(bookRepository).delete(eq(book));
    }

}
