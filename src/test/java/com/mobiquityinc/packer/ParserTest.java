package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.model.Item;
import com.mobiquityinc.packer.model.Pack;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTest {
    public static final String TEST_INPUT_FILE = "test_input_sample.txt";
    private static final String TEST_PACKAGE = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)";
    private static final String TEST_ITEM = "(1,15.3,€34)";
    private static final float COMPARING_EPSILON = 0.001f;
    
    @Test
    public void parseInputTest() throws APIException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource(TEST_INPUT_FILE);
        final List<Pack> packages = Parser.parseInput(resource.getFile());
        
        Assertions.assertEquals(4, packages.size());
    }
    
    @Test
    public void parsePackageTest() throws APIException {
        final Pack pack = Parser.parsePackage(TEST_PACKAGE);
        
        Assertions.assertEquals(81, pack.getMaxWeight(), COMPARING_EPSILON);
        Assertions.assertEquals(6, pack.getItems().size());
    }
    
    @Test
    public void parseItemTest() throws APIException {
        final Item item = Parser.parseItem(TEST_ITEM);
        
        Assertions.assertEquals(1, item.getIndex());
        Assertions.assertEquals(15.3, item.getWeight(), COMPARING_EPSILON);
        Assertions.assertEquals(new BigDecimal(34), item.getPrice());
        Assertions.assertEquals(2.222222, item.getSpecificPrice(), COMPARING_EPSILON);   
    }
}
