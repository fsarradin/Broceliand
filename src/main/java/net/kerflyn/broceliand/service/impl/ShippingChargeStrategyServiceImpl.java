package net.kerflyn.broceliand.service.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;
import net.kerflyn.broceliand.repository.ShippingChargeStrategyRepository;
import net.kerflyn.broceliand.service.ShippingChargeStrategyService;

import java.util.Set;

public class ShippingChargeStrategyServiceImpl implements ShippingChargeStrategyService {

    private ShippingChargeStrategyRepository shippingChargeStrategyRepository;

    @Inject
    public ShippingChargeStrategyServiceImpl(ShippingChargeStrategyRepository shippingChargeStrategyRepository) {
        this.shippingChargeStrategyRepository = shippingChargeStrategyRepository;
    }

    @Override
    @Transactional
    public void remove(ShippingChargeStrategy strategy) {
        shippingChargeStrategyRepository.delete(strategy);
    }

    @Override
    public void saveAll(Set<ShippingChargeStrategy> strategies) {
        for (ShippingChargeStrategy strategy : strategies) {
            shippingChargeStrategyRepository.save(strategy);
        }
    }
}
