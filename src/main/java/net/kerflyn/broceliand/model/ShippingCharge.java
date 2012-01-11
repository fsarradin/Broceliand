package net.kerflyn.broceliand.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ShippingCharge {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ShippingChargeStrategy shippingChargeStrategy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShippingChargeStrategy getShippingChargeStrategy() {
        return shippingChargeStrategy;
    }

    public void setShippingChargeStrategy(ShippingChargeStrategy shippingChargeStrategy) {
        this.shippingChargeStrategy = shippingChargeStrategy;
    }
}
