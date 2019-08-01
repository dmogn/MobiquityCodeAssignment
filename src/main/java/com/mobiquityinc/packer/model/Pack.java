package com.mobiquityinc.packer.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * A package to send. 
 */
@Data
public class Pack {
    private float maxWeight;
    
    private final List<Item> items = new ArrayList<>();
    
    public Pack(float maxWeight) {
        this.maxWeight = maxWeight;
    }
}
