package net.kerflyn.broceliand.test.configuration;

import com.google.inject.Injector;
import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.BasketElementService;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class BasketElementServiceImplIntegrationTest {

    private Injector injector;

    private Book book;
    private User user;
    private BasketElementService basketElementService;

    @Before
    public void setUp() {
        injector = BroceliandTestConfiguration.newGuiceInjector();

        user = new User();
        user.setLogin("toto");
        injector.getInstance(UserService.class).save(user);

        book = new Book();
        book.setPrice(new BigDecimal("40.0"));
        injector.getInstance(BookService.class).save(book);
        basketElementService = injector.getInstance(BasketElementService.class);
    }

    @Test
    public void should_add_new_book_to_user() {
        basketElementService.addBookById(user, book.getId());

        List<BasketElement> basketElements = basketElementService.findByUser(user);
        assertThat(basketElements).onProperty("quantity").containsExactly(1);
    }

    @Test
    public void should_add_another_occurrence_of_book_to_user() {
        basketElementService.addBookById(user, book.getId());
        basketElementService.addBookById(user, book.getId());

        List<BasketElement> basketElements = basketElementService.findByUser(user);
        assertThat(basketElements).onProperty("quantity").containsExactly(2);
    }

}
