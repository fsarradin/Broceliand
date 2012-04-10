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

package net.kerflyn.broceliand.model;

import net.kerflyn.broceliand.model.charge.FixedShippingCharge;
import net.kerflyn.broceliand.model.charge.ProportionalShippingCharge;
import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;

public class SellerTest {

    private Seller seller;
    private final FixedShippingCharge charge1 = new FixedShippingCharge();
    private final FixedShippingCharge charge2 = new FixedShippingCharge();
    private final ProportionalShippingCharge charge3 = new ProportionalShippingCharge();

    @Before
    public void setUp() {
        charge1.setCharge(BigDecimal.ONE);
        charge1.setUpToQuantity(2);
        
        charge2.setCharge(new BigDecimal("2"));
        charge2.setUpToQuantity(5);

        charge3.setPriceRate(new BigDecimal("0.01"));

        ArrayList<ShippingChargeStrategy> strategies = newArrayList();
        strategies.add(charge1);
        strategies.add(charge2);
        strategies.add(charge3);

        seller = new Seller();
        seller.setShippingChargeStrategies(strategies);
    }
    
    @Test
    public void should_find_strategy_by_quantity() {
        assertThat(seller.getShippingChargeStrategyFor(1)).isEqualTo(charge1);
        assertThat(seller.getShippingChargeStrategyFor(4)).isEqualTo(charge2);
        assertThat(seller.getShippingChargeStrategyFor(8)).isEqualTo(charge3);
    }
    
}
