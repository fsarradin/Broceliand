package net.kerflyn.broceliand.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String author;

    @OneToMany
    private Set<SellerPrice> sellerPrices = newHashSet();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSellerPrices(Set<SellerPrice> sellerPrices) {
        this.sellerPrices = sellerPrices;
    }

    public Set<SellerPrice> getSellerPrices() {
        return sellerPrices;
    }

    public BigDecimal getPrice() {
        BigDecimal price = BigDecimal.ZERO;
        Iterator<SellerPrice> iterator = sellerPrices.iterator();
        if (iterator.hasNext()) {
            price = iterator.next().getPrice();
            while (iterator.hasNext()) {
                BigDecimal otherPrice = iterator.next().getPrice();
                if (otherPrice.compareTo(price) < 0) {
                    price = otherPrice;
                }
            }
        }
        return price;
    }

    public BigDecimal getPriceFor(Seller seller) {
        for (SellerPrice sellerPrice : sellerPrices) {
            if (sellerPrice.getSeller().equals(seller)) {
                return sellerPrice.getPrice();
            }
        }

        throw new NoSuchElementException();
    }
}
