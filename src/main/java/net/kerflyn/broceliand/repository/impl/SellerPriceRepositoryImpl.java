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
