/*
 * Copyright 2012 François Sarradin <fsarradin AT gmail DOT com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.kerflyn.broceliand.service.impl;

import com.google.inject.Injector;
import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.service.BasketService;
import net.kerflyn.broceliand.service.BookService;
import net.kerflyn.broceliand.service.UserService;
import net.kerflyn.broceliand.test.configuration.BroceliandTestConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class BasketServiceImplIntegrationTest {

    private Injector injector;

    private Book book;
    private User user;
    private BasketService basketService;

    @Before
    public void setUp() {
        injector = BroceliandTestConfiguration.newGuiceInjector();

        user = new User();
        user.setLogin("toto");
        injector.getInstance(UserService.class).save(user);

        book = new Book();
        injector.getInstance(BookService.class).save(book);
        basketService = injector.getInstance(BasketService.class);
    }

    @Test
    public void should_add_new_book_to_user() {
        basketService.addBookById(user, book.getId());

        List<BasketElement> basketElements = basketService.findByUser(user);
        assertThat(basketElements).onProperty("quantity").containsExactly(1);
    }

    @Test
    public void should_add_another_occurrence_of_book_to_user() {
        basketService.addBookById(user, book.getId());
        basketService.addBookById(user, book.getId());

        List<BasketElement> basketElements = basketService.findByUser(user);
        assertThat(basketElements).onProperty("quantity").containsExactly(2);
    }

}
