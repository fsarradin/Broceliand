package net.kerflyn.broceliand.service.impl;

import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BookServiceImplWhenDelete {

    private BookRepository bookRepository;
    private BookServiceImpl bookService;

    @Before
    public void setUp() throws Exception {
        bookRepository = mock(BookRepository.class);
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    public void should_delete_book() {
        Book book = new Book();
        bookService.delete(book);

        verify(bookRepository).delete(eq(book));
    }

}
