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
import net.kerflyn.broceliand.model.BasketElement;
import net.kerflyn.broceliand.model.Book;
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.BasketElementRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class BasketElementRepositoryImpl implements BasketElementRepository {

    @Inject
    EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<BasketElement> findByUser(User user) {
        final Query query = entityManager.createQuery("select be from BasketElement be where be.owner = :user");
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public long countByUser(User user) {
        final Query query = entityManager.createQuery("select sum(be.quantity) from BasketElement be where be.owner = :user");
        query.setParameter("user", user);
        final Long count = (Long) query.getSingleResult();
        return count == null ? 0L : count;
    }

    @Override
    public BasketElement findByUserAndBook(User user, Book book) {
        final Query query = entityManager.createQuery("select be from BasketElement be where be.owner = :user and be.book = :book");
        query.setParameter("user", user);
        query.setParameter("book", book);
        return (BasketElement) query.getSingleResult();
    }

    @Override
    public void save(BasketElement basketElement) {
        entityManager.persist(basketElement);
    }

    @Override
    public void delete(BasketElement basketElement) {
        entityManager.remove(basketElement);
    }

}
