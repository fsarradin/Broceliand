package net.kerflyn.broceliand.model.charge;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Shipping charge proportional to the quantity you have to ship.
 */
@Entity
@DiscriminatorValue("PROPORTIONAL")
public class ProportionalShippingCharge extends ShippingChargeStrategy {

    private BigDecimal priceRate;

    public BigDecimal getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(BigDecimal priceRate) {
        this.priceRate = priceRate;
    }
}
