package net.kerflyn.broceliand.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class BasketElement {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Seller seller;

    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return book.findPriceOf(seller);
    }

    public BigDecimal getPrice() {
        return getUnitPrice().multiply(new BigDecimal(quantity));
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Seller getSeller() {
        return seller;
    }
}
