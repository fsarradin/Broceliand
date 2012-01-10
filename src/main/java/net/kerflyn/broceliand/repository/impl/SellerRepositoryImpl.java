package net.kerflyn.broceliand.repository.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
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
    @Transactional
    @SuppressWarnings("unchecked")
    public List<Seller> findAll() {
        return entityManager.createQuery("select s from Seller s").getResultList();
    }

    @Override
    @Transactional
    public void save(Seller seller) {
        entityManager.persist(seller);
    }

    @Override
    @Transactional
    public void deleteById(Long sellerId) {
        Seller seller = findById(sellerId);
        entityManager.remove(seller);
    }

    @Override
    @Transactional
    public Seller findById(Long sellerId) {
        return entityManager.find(Seller.class, sellerId);
    }


}
