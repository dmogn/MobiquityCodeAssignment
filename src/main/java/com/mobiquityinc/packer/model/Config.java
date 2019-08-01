package com.mobiquityinc.packer.model;

import java.math.BigDecimal;

/**
 * Configuration constants.
 */
public class Config {
    public static final float MAX_PACKAGE_WEIGHT = 100;
    
    public static final int MAX_PACKAGE_ITEMS_COUNT = 15;
    
    public static final float MAX_ITEM_WEIGHT = 100;
    
    public static final BigDecimal MAX_ITEM_COST = new BigDecimal(100);
}
