package net.kerflyn.broceliand.model;

import java.math.BigDecimal;

public class Invoice {

    private Iterable<BasketElement> basketElements;

    public Invoice(Iterable<BasketElement> basketElements) {
        this.basketElements = basketElements;
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (BasketElement element : basketElements) {
            final BigDecimal bookUnitPrice = element.getBook().getPrice();
            final BigDecimal quantity = new BigDecimal(element.getQuantity());
            total = total.add(bookUnitPrice.multiply(quantity));
        }

        return total;
    }
}
