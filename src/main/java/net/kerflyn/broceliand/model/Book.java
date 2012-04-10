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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String author;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<SellerPrice> sellerPrices = newHashSet();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSellerPrices(Set<SellerPrice> sellerPrices) {
        this.sellerPrices = sellerPrices;
    }

    public Set<SellerPrice> getSellerPrices() {
        return sellerPrices;
    }

    /**
     * Get the lowest price among sellers for this book.
     *
     * @return
     */
    public SellerPrice getLowestSellerPrice() {
        SellerPrice sellerPrice = null;
        Iterator<SellerPrice> iterator = sellerPrices.iterator();
        if (iterator.hasNext()) {
            sellerPrice = iterator.next();
            while (iterator.hasNext()) {
                SellerPrice otherSellerPrice = iterator.next();
                if (otherSellerPrice.getPrice().compareTo(sellerPrice.getPrice()) < 0) {
                    sellerPrice = otherSellerPrice;
                }
            }
        }
        return sellerPrice;
    }

    /**
     * Find the price of this book for a given seller.
     *
     * @param seller seller to get the price for
     * @return a price
     */
    public BigDecimal findPriceOf(Seller seller) {
        for (SellerPrice sellerPrice : sellerPrices) {
            if (sellerPrice.getSeller().equals(seller)) {
                return sellerPrice.getPrice();
            }
        }

        throw new NoSuchElementException();
    }
}
