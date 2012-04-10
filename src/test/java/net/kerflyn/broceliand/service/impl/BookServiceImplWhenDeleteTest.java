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
        bookService = new BookServiceImpl(bookRepository, sellerPriceRepository);
    }

    @Test
    public void should_delete_book() {
        Book book = new Book();
        bookService.delete(book);

        verify(bookRepository).delete(eq(book));
    }

}
