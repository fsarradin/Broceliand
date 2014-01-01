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

import net.kerflyn.broceliand.controller.model.ShippingChargeStrategyValueObject;
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.charge.FixedShippingCharge;
import net.kerflyn.broceliand.model.charge.ProportionalShippingCharge;
import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;
import org.simpleframework.http.Part;
import org.simpleframework.http.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ShippingCharges {

    public static final String POLICY_FIXED = "Fixed";

    public static final String POLICY_PROPORTIONAL = "Proportional";

    private static final BigDecimal BIGDECIMAL_HUNDRED = new BigDecimal("100.00");

    private ShippingCharges() {
        throw new UnsupportedOperationException();
    }

    public static Set<ShippingChargeStrategy> instantiateFrom(Request request) throws IOException {
        Map<String, Map<String, String>> strategyData = classifyStrategyData(request);

        HashSet<ShippingChargeStrategy> strategies = new HashSet<ShippingChargeStrategy>();

        for (Map<String, String> data: strategyData.values()) {
            ShippingChargeStrategy strategy = null;

            if (POLICY_FIXED.equals(data.get("policy"))) {
                strategy = createFixedShippingChargeFrom(data);
            }
            else if (POLICY_PROPORTIONAL.equals(data.get("policy"))) {
                strategy = createProportionalShippingChargeFrom(data);
            }

            strategies.add(strategy);
        }

        return strategies;
    }

    private static Map<String, Map<String, String>> classifyStrategyData(Request request) throws IOException {
        Map<String, Map<String, String>> strategyData = new HashMap<String, Map<String, String>>();

        for (Part part : request.getParts()) {
            String name = part.getName();

            if (!name.startsWith("shipping-charge")) {
                continue;
            }

            String index = getIndexFrom(name);
            String field = getFieldFrom(name);

            if (!strategyData.containsKey(index)) {
                strategyData.put(index, new HashMap<String, String>());
            }
            strategyData.get(index).put(field, part.getContent());
        }
        return strategyData;
    }

    private static String getFieldFrom(String key) {
        int indexSepLoc = key.lastIndexOf("-");
        String keyWithoutIndex = key.substring(0, indexSepLoc);
        return key.substring(keyWithoutIndex.lastIndexOf("-") + 1, indexSepLoc);
    }

    private static String getIndexFrom(String key) {
        return key.substring(key.lastIndexOf("-") + 1);
    }

    private static FixedShippingCharge createFixedShippingChargeFrom(Map<String, String> data) {
        FixedShippingCharge strategy = new FixedShippingCharge();
        strategy.setCharge(new BigDecimal(data.get("price")));
        strategy.setUpToQuantity(Integer.parseInt(data.get("quantity")));
        return strategy;
    }

    private static ProportionalShippingCharge createProportionalShippingChargeFrom(Map<String, String> data) {
        ProportionalShippingCharge strategy = new ProportionalShippingCharge();
        strategy.setPriceRate(new BigDecimal(data.get("rate")).divide(BIGDECIMAL_HUNDRED));
        strategy.setUpToQuantity(Integer.parseInt(data.get("quantity")));
        return strategy;
    }

    public static List<ShippingChargeStrategyValueObject> getShippingChargeStrategyFrom(Seller seller) {
        List<ShippingChargeStrategyValueObject> strategies = new ArrayList<ShippingChargeStrategyValueObject>();
        long index = 0;

        for (ShippingChargeStrategy strategy : seller.getShippingChargeStrategies()) {
            ShippingChargeStrategyValueObject valueObject = new ShippingChargeStrategyValueObject();

            valueObject.setId(index);
            valueObject.setQuantity(strategy.getUpToQuantity());

            if (strategy instanceof FixedShippingCharge) {
                valueObject.setPolicy(POLICY_FIXED);
                valueObject.setPrice(((FixedShippingCharge) strategy).getCharge());
            }
            else if (strategy instanceof ProportionalShippingCharge) {
                valueObject.setPolicy(POLICY_PROPORTIONAL);
                valueObject.setRate(((ProportionalShippingCharge) strategy).getPriceRate().multiply(BIGDECIMAL_HUNDRED));
            }

            strategies.add(valueObject);
            index++;
        }

        return strategies;
    }
}
