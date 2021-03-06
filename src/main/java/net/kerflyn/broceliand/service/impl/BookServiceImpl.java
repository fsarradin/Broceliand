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

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.SellerPrice;
import net.kerflyn.broceliand.repository.BookRepository;
import net.kerflyn.broceliand.repository.SellerPriceRepository;
import net.kerflyn.broceliand.service.BookService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private SellerPriceRepository sellerPriceRepository;

    @Inject
    public BookServiceImpl(BookRepository bookRepository,
                           SellerPriceRepository sellerPriceRepository) {
        this.bookRepository = bookRepository;
        this.sellerPriceRepository = sellerPriceRepository;
    }

    @Override
    @Transactional
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<SellerPrice> findPricesFor(Book book) {
        return sellerPriceRepository.findAllByBook(book);
    }

    @Override
    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book findById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    @Transactional
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    @Transactional
    public void deleteById(Long bookId) {
        Book book = bookRepository.findById(bookId);
        for (SellerPrice sellerPrice: book.getSellerPrices()) {
            sellerPriceRepository.delete(sellerPrice);
        }
        bookRepository.delete(book);
    }

    @Override
    public void setPrice(Book book, Seller seller, BigDecimal price) {
        SellerPrice sellerPrice = findSellerPriceBySeller(book, seller);
        if (sellerPrice == null) {
            sellerPrice = new SellerPrice();
            sellerPrice.setBook(book);
            sellerPrice.setSeller(seller);

            sellerPriceRepository.save(sellerPrice);

            book.getSellerPrices().add(sellerPrice);
        }
        sellerPrice.setPrice(price);
    }

    @Override
    public void removeSellerPricesNotIn(Set<Seller> sellers, Book book) {
        List<SellerPrice> sellerPrices = findPricesFor(book);
        for (SellerPrice sellerPrice: sellerPrices) {
            if (!sellers.contains(sellerPrice.getSeller())) {
                sellerPriceRepository.delete(sellerPrice);
            }
        }
    }

    private SellerPrice findSellerPriceBySeller(Book book, Seller seller) {
        for (SellerPrice sellerPrice: findPricesFor(book)) {
            if (seller.getId().equals(sellerPrice.getSeller().getId())) {
                return sellerPrice;
            }
        }

        return null;
    }

}
