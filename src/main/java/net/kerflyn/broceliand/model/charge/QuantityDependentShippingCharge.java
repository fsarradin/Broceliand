package net.kerflyn.broceliand.model.charge;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("QUANTITY_DEPENDENT")
public class QuantityDependentShippingCharge extends ShippingChargeStrategy {

    private BigDecimal initialCharge;

    private BigDecimal increase;

    private int quantityStep;

    public BigDecimal getInitialCharge() {
        return initialCharge;
    }

    public void setInitialCharge(BigDecimal initialCharge) {
        this.initialCharge = initialCharge;
    }

    public BigDecimal getIncrease() {
        return increase;
    }

    public void setIncrease(BigDecimal increase) {
        this.increase = increase;
    }

    public int getQuantityStep() {
        return quantityStep;
    }

    public void setQuantityStep(int quantityStep) {
        this.quantityStep = quantityStep;
    }
}
