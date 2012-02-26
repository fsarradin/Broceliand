package net.kerflyn.broceliand.model.charge;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("NONE")
public class NoShippingCharge extends ShippingChargeStrategy {
}
