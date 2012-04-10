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

package net.kerflyn.broceliand.repository.impl;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.SellerPrice;
import net.kerflyn.broceliand.repository.SellerPriceRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class SellerPriceRepositoryImpl implements SellerPriceRepository {

    private EntityManager entityManager;

    @Inject
    public SellerPriceRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SellerPrice> findAllByBook(Book book) {
        Query query = entityManager.createQuery("select p from SellerPrice p where p.book = :book");
        query.setParameter("book", book);
        return query.getResultList();
    }

    @Override
    public void save(SellerPrice sellerPrice) {
        entityManager.persist(sellerPrice);
    }

    @Override
    public void delete(SellerPrice sellerPrice) {
        entityManager.remove(sellerPrice);
    }

}
