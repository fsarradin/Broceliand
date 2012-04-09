package net.kerflyn.broceliand.controller.model;

import java.math.BigDecimal;

public class ShippingChargeStrategyValueObject {

    private Long id;

    private String policy;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal rate;

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getPolicy() {
        return policy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
