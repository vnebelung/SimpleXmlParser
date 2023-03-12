package main;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class SimpleXmlParserTest {

    @Test
    public void testCreateHtml() throws IOException {
        Path tmp = File.createTempFile("xml", null).toPath();
        Document document = SimpleXmlParser.createHtml();
        document.toXml(tmp);
        List<String> lines = Files.readAllLines(tmp);

        Assert.assertEquals(lines.get(0), "<!DOCTYPE html>");

        Files.delete(tmp);
    }

    @Test
    public void testCreateXml() throws IOException {
        Path tmp = File.createTempFile("xml", null).toPath();
        Document document = SimpleXmlParser.createXml();
        document.toXml(tmp);
        List<String> lines = Files.readAllLines(tmp);

        Assert.assertEquals(lines.get(0), "<?xml version=\"1.0\" ?>");

        Files.delete(tmp);
    }

    @Test
    public void testParse() throws URISyntaxException {
        Path xml = Paths.get(Objects.requireNonNull(getClass().getResource("/xml1.xml")).toURI());
        try {
            Document document = SimpleXmlParser.parse(xml);
            Assert.assertNotNull(document);
        } catch (ParseException e) {
            Assert.fail();
        }
    }

}
