package net.kerflyn.broceliand.util;

import net.kerflyn.broceliand.model.charge.FixedShippingCharge;
import net.kerflyn.broceliand.model.charge.ProportionalShippingCharge;
import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class LoadShippingChargeTest {

    @Test
    public void should_instantiate_shipping_charge() {
        Map<String, String> form = new HashMap<String, String>() {{
            put("shipping-charge-policy-0", "Fixed");
            put("shipping-charge-price-0", "5");
            put("shipping-charge-quantity-0", "99");

            put("seller-price", "42");

            put("shipping-charge-policy-1", "Proportional");
            put("shipping-charge-rate-1", "10");
            put("shipping-charge-quantity-1", "99");
        }};

        Set<ShippingChargeStrategy> strategies = ShippingCharges.instantiateFrom(form);

        assertThat(strategies).isNotNull();
        assertThat(strategies).hasSize(2);
        assertThat(strategies).onProperty("class").containsOnly(FixedShippingCharge.class, ProportionalShippingCharge.class);
    }

}
