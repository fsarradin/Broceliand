package net.kerflyn.broceliand.service;

import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;

import java.util.Set;

public interface ShippingChargeStrategyService {

    void remove(ShippingChargeStrategy strategy);

    void saveAll(Set<ShippingChargeStrategy> strategies);
}
