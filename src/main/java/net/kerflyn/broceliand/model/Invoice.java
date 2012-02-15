package net.kerflyn.broceliand.model;

import net.kerflyn.broceliand.model.charge.FixedShippingCharge;
import net.kerflyn.broceliand.model.charge.ProportionalShippingCharge;
import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;

import java.math.BigDecimal;

public class Invoice {

    private Iterable<BasketElement> basketElements;

    public Invoice(Iterable<BasketElement> basketElements) {
        this.basketElements = basketElements;
    }

    public Iterable<BasketElement> getBasketElements() {
        return basketElements;
    }

    public BigDecimal getSubTotal() {
        BigDecimal subTotal = BigDecimal.ZERO;

        for (BasketElement element : basketElements) {
            subTotal = subTotal.add(element.getPrice());
        }

        return subTotal;
    }

    public BigDecimal getShippingCharge() {
        BigDecimal charge = BigDecimal.ZERO;

        for (BasketElement element : basketElements) {
            ShippingChargeStrategy strategy = element.getSeller().getShippingChargeStrategyFor(element.getQuantity());
            if (strategy instanceof FixedShippingCharge) {
                FixedShippingCharge fixed = (FixedShippingCharge) strategy;
                charge = charge.add(fixed.getCharge());
            } else if (strategy instanceof ProportionalShippingCharge) {
                ProportionalShippingCharge proportional = (ProportionalShippingCharge) strategy;
                charge = charge.add(proportional.getPriceRate().multiply(element.getPrice()));
            }
        }

        return charge;
    }

    public BigDecimal getTotal() {
        return getSubTotal().add(getShippingCharge());
    }
}
