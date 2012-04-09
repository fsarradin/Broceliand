package net.kerflyn.broceliand.model;

import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Entity
public class Seller {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String address;

    private String city;

    private String country;

    @ManyToMany
    private Set<Book> books;

    @OneToMany
    private List<ShippingChargeStrategy> shippingChargeStrategies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<ShippingChargeStrategy> getShippingChargeStrategies() {
        return shippingChargeStrategies;
    }

    public void setShippingChargeStrategies(List<ShippingChargeStrategy> shippingChargeStrategies) {
        this.shippingChargeStrategies = shippingChargeStrategies;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    /**
     * Get the shipping charge strategy for the given quantity.
     *
     * @param quantity
     * @return
     */
    public ShippingChargeStrategy getShippingChargeStrategyFor(int quantity) {
        Iterator<? extends ShippingChargeStrategy> iterator = shippingChargeStrategies.iterator();
        ShippingChargeStrategy strategy = null;
        
        while (iterator.hasNext()) {
            ShippingChargeStrategy newStrategy = iterator.next();
            if (isNextStrategyMoreAppropriateFor(quantity, newStrategy, strategy)) {
                strategy = newStrategy;
            }
        }

        return strategy;
    }

    /**
     * Check is the next strategy is more appropriate than the previous one according to the given quantity.
     *
     * @param quantity
     * @param nextStrategy
     * @param previousStrategy
     * @return
     */
    private boolean isNextStrategyMoreAppropriateFor(int quantity, ShippingChargeStrategy nextStrategy, ShippingChargeStrategy previousStrategy) {
        Integer nextUpToQuantity = nextStrategy.getUpToQuantity();
        if (nextUpToQuantity == null) {
            return previousStrategy == null;
        } else if (quantity <= nextUpToQuantity)  {
            return previousStrategy == null
                    || previousStrategy.getUpToQuantity() == null
                    || nextUpToQuantity < previousStrategy.getUpToQuantity();
        }
        return false;
    }

}
