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

package net.kerflyn.broceliand.service;

import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.SellerPrice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface BookService {
    List<Book> findAll();

    List<SellerPrice> findPricesFor(Book book);

    void save(Book book);

    Book findById(Long bookId);

    void delete(Book book);

    void deleteById(Long bookId);

    void setPrice(Book book, Seller seller, BigDecimal price);

    void removeSellerPricesNotIn(Set<Seller> sellers, Book book);
}
