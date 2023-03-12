package internal;

import main.Document;
import main.Element;
import main.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ParserTest {

    @Test
    public void testParse() throws URISyntaxException {
        Parser parser = new Parser();
        try {
            Document document =
                    parser.parse(Paths.get(Objects.requireNonNull(getClass().getResource("/xml1.xml")).toURI()));
            Assert.assertEquals(document.getRoot().getName(), "root");

            Assert.assertEquals(document.getRoot().getAttributes().size(), 3);
            Assert.assertEquals(document.getRoot().getAttributes().get("rootkey1"), "rootvalue1");
            Assert.assertEquals(document.getRoot().getAttributes().get("rootkey2"), "");
            Assert.assertEquals(document.getRoot().getAttributes().get("rootkey3"), "rootvalue3");

            Assert.assertEquals(document.getRoot().getChildTexts().size(), 1);
            Assert.assertEquals(document.getRoot().getChildTexts().get(0), "TEXT1");

            Assert.assertEquals(document.getRoot().getChildElements().size(), 3);

            Element child1 = document.getRoot().getChildElements().get(0);
            Assert.assertEquals(child1.getName(), "child1");
            Assert.assertTrue(child1.getChildTexts().isEmpty());
            Assert.assertTrue(child1.getAttributes().isEmpty());
            Assert.assertTrue(child1.getChildElements().isEmpty());

            Element child2 = document.getRoot().getChildElements().get(1);
            Assert.assertEquals(child2.getName(), "child2");
            Assert.assertEquals(child2.getAttributes().size(), 2);
            Assert.assertEquals(child2.getAttributes().get("child2key1"), "");
            Assert.assertEquals(child2.getAttributes().get("child2key2"), "child2value2");
            Assert.assertTrue(child2.getChildTexts().isEmpty());
            Assert.assertTrue(child2.getChildElements().isEmpty());

            Element child3 = document.getRoot().getChildElements().get(2);
            Assert.assertEquals(child3.getName(), "child3");
            Assert.assertTrue(child3.getAttributes().isEmpty());
            Assert.assertTrue(child3.getChildTexts().isEmpty());
            Assert.assertEquals(child3.getChildElements().size(), 2);

            Element child4 = child3.getChildElements().get(0);
            Assert.assertEquals(child4.getName(), "child4");
            Assert.assertTrue(child4.getAttributes().isEmpty());
            Assert.assertTrue(child4.getChildTexts().isEmpty());
            Assert.assertTrue(child4.getChildElements().isEmpty());

            Element child5 = child3.getChildElements().get(1);
            Assert.assertEquals(child5.getName(), "child5");
            Assert.assertTrue(child5.getAttributes().isEmpty());
            Assert.assertEquals(child5.getChildTexts().size(), 1);
            Assert.assertEquals(child5.getChildTexts().get(0), "TEXT2");
            Assert.assertTrue(child5.getChildElements().isEmpty());
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test(expectedExceptions = ParseException.class, expectedExceptionsMessageRegExp = "Error in line 1 at column 6:.*")
    public void testParse9() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/xml2.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class, expectedExceptionsMessageRegExp = "Error in line 2 at column 2:.*")
    public void testParse10() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/xml3.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class, expectedExceptionsMessageRegExp = "Error in line 1 at column 18:" +
            ".*")
    public void testParse11() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/xml4.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class, expectedExceptionsMessageRegExp = "Error in line 1 at column 20:" +
            ".*")
    public void testParse12() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/xml5.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class, expectedExceptionsMessageRegExp = "Error in line 1 at column 21:" +
            ".*")
    public void testParse13() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/xml6.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class, expectedExceptionsMessageRegExp = "Error in line 2 at column 1:.*")
    public void testParse15() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/xml8.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class, expectedExceptionsMessageRegExp = "Error in line 1 at column 2:.*")
    public void testParse16() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/xml9.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class, expectedExceptionsMessageRegExp = "Error in line 6 at column 26:.*")
    public void testParse14() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/xml7.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class, expectedExceptionsMessageRegExp = "Error in line 1 at column 15:.*")
    public void testParse1() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/html2.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class, expectedExceptionsMessageRegExp = "Error in line 2 at column 2:.*")
    public void testParse2() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/html3.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class,
            expectedExceptionsMessageRegExp = "Error in line 2 at column 23:.*")
    public void testParse3() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/html4.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class,
            expectedExceptionsMessageRegExp = "Error in line 2 at column 23:.*")
    public void testParse4() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/html5.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class,
            expectedExceptionsMessageRegExp = "Error in line 5 at column 58:.*")
    public void testParse5() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/html6.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class,
            expectedExceptionsMessageRegExp = "Error in line 8 at column 11:.*")
    public void testParse6() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/html7.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class,
            expectedExceptionsMessageRegExp = "Error in line 8 at column 20:.*")
    public void testParse7() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/html8.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test(expectedExceptions = ParseException.class,
            expectedExceptionsMessageRegExp = "Error in line 10 at column 1:.*")
    public void testParse8() throws URISyntaxException, ParseException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/html9.xml")).toURI());
        Parser parser = new Parser();
        parser.parse(xml);
    }

    @Test
    public void testIsStartCharacter() {
        Assert.assertTrue(Parser.isStartCharacter(':'));
        Assert.assertTrue(Parser.isStartCharacter('A'));
        Assert.assertTrue(Parser.isStartCharacter('_'));
        Assert.assertTrue(Parser.isStartCharacter('a'));
        Assert.assertTrue(Parser.isStartCharacter('À'));
        Assert.assertTrue(Parser.isStartCharacter('Ø'));
        Assert.assertTrue(Parser.isStartCharacter('ø'));
        Assert.assertTrue(Parser.isStartCharacter('Ͱ'));
        Assert.assertTrue(Parser.isStartCharacter('Ϳ'));
        Assert.assertTrue(Parser.isStartCharacter(0x200d));
        Assert.assertTrue(Parser.isStartCharacter('⁰'));
        Assert.assertTrue(Parser.isStartCharacter('Ⰰ'));
        Assert.assertTrue(Parser.isStartCharacter('、'));
        Assert.assertTrue(Parser.isStartCharacter('豈'));
        Assert.assertTrue(Parser.isStartCharacter('ﷰ'));
        Assert.assertTrue(Parser.isStartCharacter(0x10000));
    }

    @Test
    public void testIsCharacter() {
        Assert.assertTrue(Parser.isCharacter('-'));
        Assert.assertTrue(Parser.isCharacter('.'));
        Assert.assertTrue(Parser.isCharacter('0'));
        Assert.assertTrue(Parser.isCharacter('·'));
        Assert.assertTrue(Parser.isCharacter('̀'));
        Assert.assertTrue(Parser.isCharacter('‿'));
    }

}
