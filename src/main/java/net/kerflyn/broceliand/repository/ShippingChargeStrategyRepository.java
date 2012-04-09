package net.kerflyn.broceliand.repository;

import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;

public interface ShippingChargeStrategyRepository {

    void delete(ShippingChargeStrategy strategy);

    void save(ShippingChargeStrategy strategy);

}
