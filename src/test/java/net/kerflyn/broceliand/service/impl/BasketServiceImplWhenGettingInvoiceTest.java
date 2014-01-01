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
import net.kerflyn.broceliand.model.Invoice;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.SellerPrice;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.BasketElementRepository;
import net.kerflyn.broceliand.service.BookService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BasketServiceImplWhenGettingInvoiceTest {

    private BasketServiceImpl basketService;
    private Book book;
    private User user;
    private Seller seller;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1L);

        book = new Book();
        book.setId(1L);
        book.setTitle("Toto");

        seller = new Seller();

        SellerPrice sellerPrice = new SellerPrice();
        sellerPrice.setPrice(new BigDecimal("40.00"));
        sellerPrice.setBook(book);
        sellerPrice.setSeller(seller);
        book.getSellerPrices().add(sellerPrice);

        BasketElementRepository basketElementRepository = mock(BasketElementRepository.class);
        BookService bookService = mock(BookService.class);
        when(bookService.findById(eq(1L))).thenReturn(book);

        basketService = new BasketServiceImpl(basketElementRepository, bookService);
    }

    @Test
    public void should_invoice_total_depend_on_basket_content() {
        BasketElement basketElement1 = new BasketElement();
        basketElement1.setOwner(user);
        basketElement1.setBook(book);
        basketElement1.setSeller(seller);
        basketElement1.setQuantity(5);

        when(basketService.findByUser(eq(user))).thenReturn(Arrays.asList(basketElement1));

        Invoice invoice = basketService.getCurrentInvoiceFor(user);
        assertThat(invoice.getSubTotal()).isEqualTo(new BigDecimal("200.00"));

        basketElement1.setQuantity(1);
        invoice = basketService.getCurrentInvoiceFor(user);
        assertThat(invoice.getSubTotal()).isEqualTo(new BigDecimal("40.00"));
    }
}
