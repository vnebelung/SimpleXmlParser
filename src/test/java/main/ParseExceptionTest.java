package main;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ParseExceptionTest {

    @Test
    public void test() {
        ParseException parseException = new ParseException("STRING", 1, 2);
        Assert.assertEquals(parseException.getMessage(), "Error in line 1 at column 2: STRING");
    }

}
