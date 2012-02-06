package net.kerflyn.broceliand.loader;

import java.util.Map;
import java.util.Set;

public class BookObjectAccessor {
    private Map<String, Map<String, Map<String, Set<BookObject>>>> catalog;

    public BookObjectAccessor(Map<String, Map<String, Map<String, Set<BookObject>>>> catalog) {
        this.catalog = catalog;
    }

    public Set<BookObject> get(String country, String city, String seller) {

        if (catalog.containsKey(country)) {
            Map<String, Map<String, Set<BookObject>>> cities = catalog.get(country);
            if (cities.containsKey(city)) {
                Map<String, Set<BookObject>> sellers = cities.get(city);
                if (sellers.containsKey(seller)) {
                    return sellers.get(seller);
                }
            }
        }

        return null;
    }
}
