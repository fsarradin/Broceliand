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

package net.kerflyn.broceliand.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.fest.assertions.Assertions.assertThat;

public class BasketElementTest {

    private BasketElement basketElement;
    private Book book;
    private Seller seller1;
    private Seller seller2;

    @Before
    public void setUp() {
        book = new Book();

        seller1 = new Seller();
        seller2 = new Seller();

        SellerPrice sellerPrice;

        sellerPrice = new SellerPrice();
        sellerPrice.setSeller(seller1);
        sellerPrice.setBook(book);
        sellerPrice.setPrice(new BigDecimal("40"));
        book.getSellerPrices().add(sellerPrice);

        sellerPrice = new SellerPrice();
        sellerPrice.setSeller(seller2);
        sellerPrice.setBook(book);
        sellerPrice.setPrice(new BigDecimal("30"));
        book.getSellerPrices().add(sellerPrice);

        basketElement = new BasketElement();
        basketElement.setBook(book);
    }

    @Test
    public void should_unit_price() {
        basketElement.setQuantity(2);
        basketElement.setSeller(seller1);

        assertThat(basketElement.getPrice()).isEqualTo(new BigDecimal("80"));

        basketElement.setQuantity(4);
        basketElement.setSeller(seller2);

        assertThat(basketElement.getPrice()).isEqualTo(new BigDecimal("120"));
    }

}
