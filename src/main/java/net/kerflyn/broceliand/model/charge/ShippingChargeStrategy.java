package net.kerflyn.broceliand.model.charge;

import net.kerflyn.broceliand.model.Seller;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public abstract class ShippingChargeStrategy {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Seller seller;

    @Column(nullable = true)
    private Integer upToQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Integer getUpToQuantity() {
        return upToQuantity;
    }

    public void setUpToQuantity(Integer upToQuantity) {
        this.upToQuantity = upToQuantity;
    }
}
