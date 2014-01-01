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

import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.BasketElementRepository;
import net.kerflyn.broceliand.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.persistence.NoResultException;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BasketServiceImplWhenAddingBookTest {

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

        basketElementRepository = mock(BasketElementRepository.class);

        bookService = mock(BookService.class);
        when(bookService.findById(eq(1L))).thenReturn(book);

        basketService = new BasketServiceImpl(basketElementRepository, bookService);
    }

    @Test
    public void should_add_new_book_to_user() {
        when(basketElementRepository.findByUserAndBook(eq(user), eq(book))).thenThrow(new NoResultException());

        basketService.addBookById(user, 1L);

        ArgumentCaptor<BasketElement> basketElementCaptor = ArgumentCaptor.forClass(BasketElement.class);
        verify(basketElementRepository).save(basketElementCaptor.capture());
        assertThat(basketElementCaptor.getValue().getQuantity()).isEqualTo(1);
    }

    @Test
    public void should_add_another_occurence_book_to_user() {
        BasketElement basketElement = new BasketElement();
        basketElement.setBook(book);
        basketElement.setOwner(user);
        basketElement.setQuantity(1);
        when(basketElementRepository.findByUserAndBook(eq(user), eq(book))).thenReturn(basketElement);

        basketService.addBookById(user, 1L);

        assertThat(basketElement.getQuantity()).isEqualTo(2);
    }

}
