package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.model.Config;
import com.mobiquityinc.packer.model.Item;
import com.mobiquityinc.packer.model.Pack;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Input file parser.
 */
public class Parser {

    private Parser() {
    }

    /**
     * Parse input text file.
     * 
     * @param filePath
     * @return
     * @throws APIException 
     */
    public static List<Pack> parseInput(String filePath) throws APIException {
        final File file = new File(filePath);

        if (!file.isFile() || !file.canRead()) {
            throw new APIException("File is inaccessible");
        }

        final List<Pack> packages = new ArrayList<>();
        try {
            final Scanner sc = new Scanner(new FileInputStream(file));

            while (sc.hasNextLine()) {
                final String nextLine = sc.nextLine();

                if (nextLine.isEmpty()) 
                    continue;
                
                packages.add( parsePackage(nextLine) );
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new APIException(e.getMessage());
        }
        
        return packages;
    }
    
    protected static Pack parsePackage(String s) throws APIException {
        final Scanner sc = new Scanner(s);
        final float maxWeight = sc.nextFloat();
        final Pack pack = new Pack(maxWeight);

        sc.useDelimiter("\\s+");

        String dotDelimiter = sc.next();
        if (!dotDelimiter.equals(":")) 
            throw new APIException("Input file format error");

        while (sc.hasNext()) {
            final String itemString = sc.next();

            final Item item = parseItem(itemString);
            pack.getItems().add(item);
        }
        
        // verify values
        if (pack.getItems().size() > Config.MAX_PACKAGE_ITEMS_COUNT)
            throw new APIException("Invalid input: count of items exceeds package limit");
        
        if (pack.getMaxWeight() > Config.MAX_PACKAGE_WEIGHT)
            throw new APIException("Invalid input value: package max weight exceeds limit");
        
        return pack;
    }

    protected static Item parseItem(String s) throws APIException {
        final Scanner sc = new Scanner(s);
        sc.useDelimiter("[\\s\\(\\)\\,]+");

        final Item item = new Item();
        
        item.setIndex(sc.nextInt());
        item.setWeight(sc.nextFloat());

        final String priceWithCurrency = sc.next();

        // find currency
        final Set<Currency> currencies = Currency.getAvailableCurrencies();
        currencies.forEach(c -> {
            if (priceWithCurrency.startsWith(c.getSymbol())) {
                item.setCurrency(c);
            }
        });

        item.setPrice(new BigDecimal(priceWithCurrency.substring(1)));
        
        item.setSpecificPrice(item.getPrice().floatValue() / item.getWeight());
        
        // verify values
        if (item.getWeight() > Config.MAX_ITEM_WEIGHT)
            throw new APIException("Invalid input value: item weight exceeds limit");
        
        
        if (item.getPrice().compareTo(Config.MAX_ITEM_COST) > 0)
            throw new APIException("Invalid input value: item price exceeds limit");
        
        return item;
    }
}
