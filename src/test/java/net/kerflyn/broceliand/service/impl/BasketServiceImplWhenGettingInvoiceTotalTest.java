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
import net.kerflyn.broceliand.model.charge.FixedShippingCharge;
import net.kerflyn.broceliand.model.charge.ProportionalShippingCharge;
import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;
import net.kerflyn.broceliand.repository.BasketElementRepository;
import net.kerflyn.broceliand.service.BookService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BasketServiceImplWhenGettingInvoiceTotalTest {

    private BasketServiceImpl basketService;
    private Book book1;
    private User user;
    private Seller seller1;
    private Seller seller2;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1L);

        createSeller1();
        createSeller2();

        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Toto");

        SellerPrice sellerPrice;
        
        sellerPrice = new SellerPrice();
        sellerPrice.setPrice(new BigDecimal("40.00"));
        sellerPrice.setBook(book1);
        book1.getSellerPrices().add(sellerPrice);
        sellerPrice.setSeller(seller1);

        sellerPrice = new SellerPrice();
        sellerPrice.setPrice(new BigDecimal("30.00"));
        sellerPrice.setBook(book1);
        book1.getSellerPrices().add(sellerPrice);
        sellerPrice.setSeller(seller2);
        
        BasketElementRepository basketElementRepository = mock(BasketElementRepository.class);
        BookService bookService = mock(BookService.class);
        when(bookService.findById(eq(1L))).thenReturn(book1);

        basketService = new BasketServiceImpl(basketElementRepository, bookService);
    }

    private void createSeller1() {
        FixedShippingCharge charge1 = new FixedShippingCharge();
        charge1.setCharge(BigDecimal.ONE);
        charge1.setUpToQuantity(2);
        FixedShippingCharge charge2 = new FixedShippingCharge();
        charge2.setCharge(new BigDecimal("2"));
        charge2.setUpToQuantity(5);
        ProportionalShippingCharge charge3 = new ProportionalShippingCharge();
        charge3.setPriceRate(new BigDecimal("0.01"));

        ArrayList<ShippingChargeStrategy> strategies = newArrayList();
        strategies.add(charge1);
        strategies.add(charge2);
        strategies.add(charge3);

        seller1 = new Seller();
        seller1.setShippingChargeStrategies(strategies);
    }

    private void createSeller2() {
        FixedShippingCharge charge1 = new FixedShippingCharge();
        charge1.setCharge(new BigDecimal("2"));

        List<? extends ShippingChargeStrategy> strategies = newArrayList(charge1);

        seller2 = new Seller();
        seller2.setShippingChargeStrategies((List<ShippingChargeStrategy>) strategies);
    }

    @Test
    public void should_invoice_total_depend_on_basket_content() {
        BasketElement basketElement1 = new BasketElement();
        basketElement1.setOwner(user);
        basketElement1.setBook(book1);
        basketElement1.setSeller(seller1);
        basketElement1.setQuantity(4);

        BasketElement basketElement2 = new BasketElement();
        basketElement2.setOwner(user);
        basketElement2.setBook(book1);
        basketElement2.setSeller(seller2);
        basketElement2.setQuantity(2);

        when(basketService.findByUser(eq(user))).thenReturn(Arrays.asList(basketElement1, basketElement2));

        Invoice invoice = basketService.getCurrentInvoiceFor(user);
        assertThat(invoice.getSubTotal()).isEqualTo(new BigDecimal("220.00"));
        assertThat(invoice.getShippingCharge()).isEqualTo(new BigDecimal("4"));
        assertThat(invoice.getTotal()).isEqualTo(new BigDecimal("224.00"));
    }
}
