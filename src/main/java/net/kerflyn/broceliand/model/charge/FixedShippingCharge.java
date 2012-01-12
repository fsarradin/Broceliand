package net.kerflyn.broceliand.model.charge;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue(value = "FIXED")
public class FixedShippingCharge extends ShippingChargeStrategy {

    private BigDecimal charge;

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }
}
