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

package net.kerflyn.broceliand.loader;

import java.util.Map;
import java.util.Set;

public class SellerAccessor {
    private Map<String, Map<String, Map<String, Set<String>>>> catalog;

    public SellerAccessor(Map<String, Map<String, Map<String, Set<String>>>> catalog) {
        this.catalog = catalog;
    }

    public Set<String> get(String country, String city, String seller) {

        if (catalog.containsKey(country)) {
            Map<String, Map<String, Set<String>>> cities = catalog.get(country);
            if (cities.containsKey(city)) {
                Map<String, Set<String>> sellers = cities.get(city);
                if (sellers.containsKey(seller)) {
                    return sellers.get(seller);
                }
            }
        }

        return null;
    }
}
