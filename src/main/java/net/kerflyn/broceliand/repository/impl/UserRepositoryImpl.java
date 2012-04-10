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
import net.kerflyn.broceliand.model.User;
import net.kerflyn.broceliand.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Inject
    EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        return entityManager.createQuery("select u from User u").getResultList();
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findByLogin(String login) {
        final Query query = entityManager.createQuery("select u from User u where u.login = :login");
        query.setParameter("login", login);
        return (User) query.getSingleResult();
    }

}
