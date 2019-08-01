package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import static com.mobiquityinc.packer.ParserTest.TEST_INPUT_FILE;
import com.mobiquityinc.packer.model.Item;
import com.mobiquityinc.packer.model.Pack;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Packer algorithm unit tests.
 */
public class PackerTest {
    
    private static final String TEST_PACKAGE = "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)";
    
    private static final String TEST_PACKAGE_NO_RESULT = "5 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64) (1,85.31,€29) (2,14.55,€74) (3,9.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75)";
    
    @Test
    public void packTest()  throws APIException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource(TEST_INPUT_FILE);
        
        final String output = Packer.pack(resource.getFile());
        
        final String expectedOutput = "4\n" +
                "-\n" +
                "2,7\n" +
                "8,9";
        Assertions.assertEquals(expectedOutput, output);
    }
    
    @Test
    public void packPackageTest()  throws APIException {
        final Pack pack = Parser.parsePackage(TEST_PACKAGE);
        
        final List<Item> items = Packer.packPackage(pack);
        
        // output: 8, 9
        Assertions.assertEquals(2, items.size());
        Assertions.assertEquals(8, items.get(0).getIndex());
        Assertions.assertEquals(9, items.get(1).getIndex());
    }
    
    @Test
    public void packPackageNoResultTest()  throws APIException {
        final Pack pack = Parser.parsePackage(TEST_PACKAGE_NO_RESULT);
        
        final List<Item> items = Packer.packPackage(pack);
        
        // empty output expected
        Assertions.assertEquals(0, items.size());
    }
}
