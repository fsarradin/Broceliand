/*
 * Copyright 2012 François Sarradin <fsarradin AT gmail DOT com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.kerflyn.broceliand.model;

import net.kerflyn.broceliand.model.charge.FixedShippingCharge;
import net.kerflyn.broceliand.model.charge.ProportionalShippingCharge;
import net.kerflyn.broceliand.model.charge.ShippingChargeStrategy;

import java.math.BigDecimal;

/**
 * Invoice value object.
 *
 * <p>
 * This object is not intended to be persisted.
 * </p>
 */
public class Invoice {

    private Iterable<BasketElement> basketElements;

    public Invoice(Iterable<BasketElement> basketElements) {
        this.basketElements = basketElements;
    }

    public Iterable<BasketElement> getBasketElements() {
        return basketElements;
    }

    /**
     * Get the total without shipping charges.
     *
     * @return
     */
    public BigDecimal getSubTotal() {
        BigDecimal subTotal = BigDecimal.ZERO;

        for (BasketElement element : basketElements) {
            subTotal = subTotal.add(element.getPrice());
        }

        return subTotal;
    }

    /**
     * Shipping charge only.
     *
     * @return
     */
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

    /**
     * Get invoice total.
     *
     * @return
     */
    public BigDecimal getTotal() {
        return getSubTotal().add(getShippingCharge());
    }
}
