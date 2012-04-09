package net.kerflyn.broceliand.repository.impl;

import com.google.inject.Inject;
import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;
import net.kerflyn.broceliand.repository.ShippingChargeStrategyRepository;

import javax.persistence.EntityManager;

public class ShippingChargeStrategyRepositoryImpl implements ShippingChargeStrategyRepository {

    private EntityManager entityManager;

    @Inject
    public ShippingChargeStrategyRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void delete(ShippingChargeStrategy strategy) {
        entityManager.remove(strategy);
    }

    @Override
    public void save(ShippingChargeStrategy strategy) {
        entityManager.persist(strategy);
    }

}
