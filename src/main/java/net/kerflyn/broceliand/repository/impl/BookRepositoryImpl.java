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
import net.kerflyn.broceliand.repository.BookRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    @Inject
    EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Book> findAll() {
        return entityManager.createQuery("select b from Book b").getResultList();
    }

    @Override
    public void save(Book book) {
        entityManager.persist(book);
    }

    @Override
    public Book findById(Long bookId) {
        final Query query = entityManager.createQuery("select b from Book b where b.id = :id");
        query.setParameter("id", bookId);
        return (Book) query.getSingleResult();
    }

    @Override
    public void delete(Book book) {
        entityManager.remove(book);
    }

    @Override
    public void deleteById(Long bookId) {
        final Query query = entityManager.createQuery("delete from Book b where b.id = :id");
        query.setParameter("id", bookId);
        query.executeUpdate();
    }

}
