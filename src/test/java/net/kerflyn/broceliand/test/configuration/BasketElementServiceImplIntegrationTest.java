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
    public Book book;

    @Before
    public void setUp() {
        injector = BroceliandTestConfiguration.newGuiceInjector();

        User user = new User();
        user.setLogin("toto");
        injector.getInstance(UserService.class).save(user);

        book = new Book();
        book.setPrice(new BigDecimal("40.0"));
        injector.getInstance(BookService.class).save(book);
    }

    @Test
    public void should_add_new_book_to_user() {
        BasketElementService basketElementService = injector.getInstance(BasketElementService.class);
        UserService userService = injector.getInstance(UserService.class);

        User user = userService.findByLogin("toto");
        basketElementService.addBookById(user, book.getId());

        List<BasketElement> basketElements = basketElementService.findByUser(user);
        assertThat(basketElements).onProperty("quantity").containsExactly(1);
    }

    @Test
    public void should_add_another_occurrence_of_book_to_user() {
        BasketElementService basketElementService = injector.getInstance(BasketElementService.class);
        UserService userService = injector.getInstance(UserService.class);

        User user = userService.findByLogin("toto");
        basketElementService.addBookById(user, book.getId());
        basketElementService.addBookById(user, book.getId());

        List<BasketElement> basketElements = basketElementService.findByUser(user);
        assertThat(basketElements).onProperty("quantity").containsExactly(2);
    }

}
