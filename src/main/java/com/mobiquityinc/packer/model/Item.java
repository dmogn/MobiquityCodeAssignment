package com.mobiquityinc.packer.model;

import java.math.BigDecimal;
import java.util.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An item to send.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Comparable {
    
    private int index;
    
    private float weight;
    
    private BigDecimal price;
    
    private Currency currency;
    
    /**
     * Price/weight specific value.
     */
    private float specificPrice;

    @Override
    public int compareTo(Object o) {
        if (o instanceof Item)
            return ((Float)specificPrice).compareTo(((Item)o).getSpecificPrice());
        else
            throw new IllegalArgumentException("Argument Object should has same type");
    }
    
}
