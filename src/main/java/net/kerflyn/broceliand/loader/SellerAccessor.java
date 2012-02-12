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
