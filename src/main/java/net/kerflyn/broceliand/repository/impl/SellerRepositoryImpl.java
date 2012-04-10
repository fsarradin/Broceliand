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
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.repository.SellerRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class SellerRepositoryImpl implements SellerRepository {

    private EntityManager entityManager;

    @Inject
    public SellerRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Seller> findAll() {
        return entityManager.createQuery("select s from Seller s order by s.name").getResultList();
    }

    @Override
    public void save(Seller seller) {
        entityManager.persist(seller);
    }

    @Override
    public void deleteById(Long sellerId) {
        Seller seller = findById(sellerId);
        entityManager.remove(seller);
    }

    @Override
    public Seller findById(Long sellerId) {
        return entityManager.find(Seller.class, sellerId);
    }

}
