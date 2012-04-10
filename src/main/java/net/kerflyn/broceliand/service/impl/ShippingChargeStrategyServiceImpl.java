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

package net.kerflyn.broceliand.service.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;
import net.kerflyn.broceliand.repository.ShippingChargeStrategyRepository;
import net.kerflyn.broceliand.service.ShippingChargeStrategyService;

import java.util.Set;

public class ShippingChargeStrategyServiceImpl implements ShippingChargeStrategyService {

    private ShippingChargeStrategyRepository shippingChargeStrategyRepository;

    @Inject
    public ShippingChargeStrategyServiceImpl(ShippingChargeStrategyRepository shippingChargeStrategyRepository) {
        this.shippingChargeStrategyRepository = shippingChargeStrategyRepository;
    }

    @Override
    @Transactional
    public void remove(ShippingChargeStrategy strategy) {
        shippingChargeStrategyRepository.delete(strategy);
    }

    @Override
    public void saveAll(Set<ShippingChargeStrategy> strategies) {
        for (ShippingChargeStrategy strategy : strategies) {
            shippingChargeStrategyRepository.save(strategy);
        }
    }
}
