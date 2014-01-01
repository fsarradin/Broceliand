/*
 * Copyright 2012 François Sarradin <fsarradin AT gmail DOT com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.kerflyn.broceliand.util;

import com.google.common.collect.Lists;
import net.kerflyn.broceliand.model.charge.FixedShippingCharge;
import net.kerflyn.broceliand.model.charge.ProportionalShippingCharge;
import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;
import org.junit.Ignore;
import org.junit.Test;
import org.simpleframework.http.Part;
import org.simpleframework.http.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoadShippingChargeTest {

    @Test @Ignore
    public void should_instantiate_shipping_charge() throws Exception {
        Request request = mock(Request.class);
        when(request.getParts()).thenReturn(Lists.<Part>newArrayList(

        ));

        Map<String, String> form = new HashMap<String, String>() {{
            put("shipping-charge-policy-0", "Fixed");
            put("shipping-charge-price-0", "5");
            put("shipping-charge-quantity-0", "99");

            put("seller-price", "42");

            put("shipping-charge-policy-1", "Proportional");
            put("shipping-charge-rate-1", "10");
            put("shipping-charge-quantity-1", "99");
        }};

        Set<ShippingChargeStrategy> strategies = ShippingCharges.instantiateFrom(request);

        assertThat(strategies).isNotNull();
        assertThat(strategies).hasSize(2);
        assertThat(extractProperty("class").from(strategies)).containsOnly(FixedShippingCharge.class, ProportionalShippingCharge.class);
    }

}
