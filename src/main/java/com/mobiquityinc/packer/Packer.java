package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.model.Item;
import com.mobiquityinc.packer.model.Pack;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Packer {
    
    private static final String LINE_BREAK = "\n";

    private Packer() {
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java com.mobiquityinc.packer.Packer [input text file] ");
        }
        
        for (final String filePath : args) {
            System.out.println( pack(filePath) );
        }
    }

    public static String pack(String filePath) throws APIException {
        final List<Pack> packages = Parser.parseInput(filePath);
        
        final StringBuilder sb = new StringBuilder();
        for (final Pack pack : packages) {
            if (sb.length() > 0)
                sb.append(LINE_BREAK);
            
            // pack items
            final List<Item> packedItems = packPackage(pack);
            
            // print result
            if (packedItems.isEmpty()) {
                // no options to package
                sb.append("-");
            } else {
                // sort items by index increasing
                Set<Integer> indexes = new TreeSet<>();
                for (final Item item : packedItems) 
                    indexes.add(item.getIndex());
                
                // pring indexes
                final StringBuilder itemsStr = new StringBuilder();
                
                for (final Integer index : indexes) {
                    if (itemsStr.length() > 0)
                        itemsStr.append(",");
                    
                    itemsStr.append(index);
                }
                
                sb.append(itemsStr);
            }
        }
        return sb.toString();
    }
    
    /**
     * Pack single package.
     * 
     * @param pack package to pack
     * @return optimal sequence of items to send in the package. 
     */
    protected static List<Item> packPackage(Pack pack) {
        // list of items (sorted by specific price)
        final Item items[] = new Item[pack.getItems().size()];
        pack.getItems().toArray(items);
        Arrays.sort(items, (Item item1, Item item2) -> ((Float)item1.getSpecificPrice()).compareTo(item2.getSpecificPrice()));
        
        // search
        return tryPack(items, 0, new LinkedList<>(), pack.getMaxWeight());
    }
    
    /**
     * Recursively try to complete list, avoiding some items as option.
     * 
     * Estimated algorithm complexity O(N^2).
     * 
     * @param items list of items (sorted by specific price)
     * @param index current item index in items array
     * @param list currently added items
     * @param maxWeight total weight limit
     * @return found optimal sequence in current branch
     */
    private static List<Item> tryPack(Item items[], int index, List<Item> list, float maxWeight) {
        final List<Item> currentList = new LinkedList<>(list);
        final float currentWeight = countWeight(currentList);
        
        List<Item> maxList = currentList;
        BigDecimal maxFoundSum = countSum(maxList);
        
        if (index < items.length-1) {
            // try to build sequence without current item
            final List<Item> optionalListEx = tryPack(items, index+1, currentList, maxWeight);
            final BigDecimal sumEx = countSum(optionalListEx);

            if (sumEx.compareTo(maxFoundSum) > 0) {
                maxList = optionalListEx;
                maxFoundSum = sumEx;
            }
        }
        
        
        if (currentWeight + items[index].getWeight() <= maxWeight) {
            // try to build sequence with current item
            currentList.add(items[index]);           

            if (index < items.length-1) {
                final List<Item> optionalListIn = tryPack(items, index+1, currentList, maxWeight);
                final BigDecimal sumIn = countSum(optionalListIn);

                if (sumIn.compareTo(maxFoundSum) > 0) {
                    maxList = optionalListIn;
                    maxFoundSum = sumIn;
                }
            }
        }
        
        return maxList;
    }
    
    /**
     * Calculate total sum of items sequence.
     * 
     * @param list
     * @return sum
     */
    private static BigDecimal countSum(List<Item> list) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Item item : list) {
            sum = sum.add(item.getPrice());
        }
        return sum;
    }
    
    /**
     * Calculate total weight of items sequence.
     * 
     * @param list
     * @return total weight
     */
    private static float countWeight(List<Item> list) {
        return list.stream().collect(Collectors.summingDouble(Item::getWeight)).floatValue();
    }
}
