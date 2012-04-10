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
import net.kerflyn.broceliand.model.Seller;
import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;
import net.kerflyn.broceliand.repository.SellerRepository;
import net.kerflyn.broceliand.service.SellerService;
import net.kerflyn.broceliand.service.ShippingChargeStrategyService;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.google.common.collect.Maps.newTreeMap;

public class SellerServiceImpl implements SellerService {

    private SellerRepository sellerRepository;

    private ShippingChargeStrategyService shippingChargeStrategyService;

    @Inject
    public SellerServiceImpl(SellerRepository sellerRepository, ShippingChargeStrategyService shippingChargeStrategyService) {
        this.sellerRepository = sellerRepository;
        this.shippingChargeStrategyService = shippingChargeStrategyService;
    }

    @Override
    @Transactional
    public List<Seller> findAll() {
        return sellerRepository.findAll();
    }

    @Override
    @Transactional
    public SortedMap<String, SortedMap<String, Seller>> findAllSorted() {
        List<Seller> sellers = sellerRepository.findAll();
        SortedMap<String, SortedMap<String, Seller>> sellerMap = newTreeMap();

        for (Seller seller : sellers) {
            SortedMap<String, Seller> sellersByCity = sellerMap.get(seller.getCountry());
            if (sellersByCity == null) {
                sellersByCity = new TreeMap<String, Seller>();
                sellerMap.put(seller.getCountry(), sellersByCity);
            }
            sellersByCity.put(seller.getCity(), seller);
        }
        return sellerMap;
    }

    @Override
    @Transactional
    public void save(Seller seller) {
        sellerRepository.save(seller);
    }

    @Override
    @Transactional
    public void deleteById(Long sellerId) {
        sellerRepository.deleteById(sellerId);
    }

    @Override
    @Transactional
    public Seller findById(Long sellerId) {
        return sellerRepository.findById(sellerId);
    }

    @Override
    @Transactional
    public void setShippingChargeStrategy(Seller seller, Set<ShippingChargeStrategy> strategies) {
            for (ShippingChargeStrategy strategy: seller.getShippingChargeStrategies()) {
                shippingChargeStrategyService.remove(strategy);
            }
            seller.getShippingChargeStrategies().clear();
            seller.getShippingChargeStrategies().addAll(strategies);
    }

}
