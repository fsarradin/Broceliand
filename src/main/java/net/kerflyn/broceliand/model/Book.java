package net.kerflyn.broceliand.model;

import javax.persistence.CascadeType;
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

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE, orphanRemoval = true)
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

    /**
     * Get the lowest price among sellers for this book.
     *
     * @return
     */
    public SellerPrice getLowestSellerPrice() {
        SellerPrice sellerPrice = null;
        Iterator<SellerPrice> iterator = sellerPrices.iterator();
        if (iterator.hasNext()) {
            sellerPrice = iterator.next();
            while (iterator.hasNext()) {
                SellerPrice otherSellerPrice = iterator.next();
                if (otherSellerPrice.getPrice().compareTo(sellerPrice.getPrice()) < 0) {
                    sellerPrice = otherSellerPrice;
                }
            }
        }
        return sellerPrice;
    }

    /**
     * Find the price of this book for a given seller.
     *
     * @param seller seller to get the price for
     * @return a price
     */
    public BigDecimal findPriceOf(Seller seller) {
        for (SellerPrice sellerPrice : sellerPrices) {
            if (sellerPrice.getSeller().equals(seller)) {
                return sellerPrice.getPrice();
            }
        }

        throw new NoSuchElementException();
    }
}
