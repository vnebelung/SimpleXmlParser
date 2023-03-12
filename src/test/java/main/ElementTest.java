package main;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ElementTest {

    @Test
    public void testGetName() {
        Element element = new Element("name");
        Assert.assertEquals(element.getName(), "name");
    }

    @Test
    public void testGetName1() {
        Element element = new Element("name", "value");
        Assert.assertEquals(element.getName(), "name");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetName2() {
        new Element("\ttext");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetName3() {
        new Element("\ttext", "value");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetName4() {
        new Element("t\ttext");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetName5() {
        new Element("t\ttext", "value");
    }

    @Test
    public void testAddAttribute() {
        Element element = new Element("name").addAttribute("att");
        Assert.assertEquals(element.getAttributes().size(), 1);
        Assert.assertEquals(element.getAttributes().get("att"), "");
    }

    @Test
    public void testAddAttribute1() {
        Element element = new Element("name").addAttribute("att", "v1").addAttribute("att", "v2");
        Assert.assertEquals(element.getAttributes().size(), 1);
        Assert.assertEquals(element.getAttributes().get("att"), "v2");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddAttribute2() {
        new Element("name").addAttribute("\tatt", "v1");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddAttribute3() {
        new Element("name").addAttribute("t\tatt", "v1");
    }

    @Test
    public void testAddChild() {
        Element element = new Element("name").addChild("text");
        Assert.assertEquals(element.getChildTexts().size(), 1);
        Assert.assertEquals(element.getChildTexts().get(0), "text");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddChild1() {
        new Element("name").addChild("\t");
    }

    @Test
    public void testAddChild2() {
        Element element = new Element("name").addChild(new Element("name2"));
        Assert.assertEquals(element.getChildElements().size(), 1);
        Assert.assertEquals(element.getChildElements().get(0).getName(), "name2");
    }

    @Test
    public void testAddChild3() {
        Element element = new Element("name").addChild("value1").addChild("value2");
        Assert.assertEquals(element.getChildTexts().size(), 1);
        Assert.assertEquals(element.getChildTexts().get(0), "value1value2");
    }

    @Test
    public void testToXml() throws IOException {
        Element element = new Element("name");
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PrintWriter printWriter = new PrintWriter(byteArrayOutputStream)) {
            element.toXml(printWriter, 0);
            printWriter.flush();
            Assert.assertEquals(byteArrayOutputStream.toString(), "<name />\n");
        }
    }

    @Test
    public void testToXml1() throws IOException {
        Element element = new Element("name");
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PrintWriter printWriter = new PrintWriter(byteArrayOutputStream)) {
            element.toXml(printWriter, 2);
            printWriter.flush();
            Assert.assertEquals(byteArrayOutputStream.toString(), "    <name />\n");
        }
    }

    @Test
    public void testToXml2() throws IOException {
        Element element = new Element("name", "value");
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PrintWriter printWriter = new PrintWriter(byteArrayOutputStream)) {
            element.toXml(printWriter, 1);
            printWriter.flush();
            Assert.assertEquals(byteArrayOutputStream.toString(), "  <name>value</name>\n");
        }
    }

    @Test
    public void testToXml3() throws IOException {
        Element element = new Element("name", "value1").addChild("value2");
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PrintWriter printWriter = new PrintWriter(byteArrayOutputStream)) {
            element.toXml(printWriter, 1);
            printWriter.flush();
            Assert.assertEquals(byteArrayOutputStream.toString(), "  <name>value1value2</name>\n");
        }
    }

    @Test
    public void testToXml4() throws IOException {
        Element element =
                new Element("name", "value1").addChild("value2").addAttribute("att2").addAttribute("att1", "val1");
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PrintWriter printWriter = new PrintWriter(byteArrayOutputStream)) {
            element.toXml(printWriter, 1);
            printWriter.flush();
            Assert.assertEquals(byteArrayOutputStream.toString(), "  <name att1=\"val1\" att2>value1value2</name>\n");
        }
    }

    @Test
    public void testToXml5() throws IOException {
        Element element = new Element("name").addChild(new Element("name2").addChild(new Element("name3")));
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PrintWriter printWriter = new PrintWriter(byteArrayOutputStream)) {
            element.toXml(printWriter, 0);
            printWriter.flush();
            Assert.assertEquals(byteArrayOutputStream.toString(),
                    "<name>\n  <name2>\n    <name3 />\n  </name2>\n</name>\n");
        }
    }

    @Test
    public void testGetChildElements() {
        Element element = new Element("name");
        Assert.assertEquals(element.getChildElements().size(), 0);
    }

    @Test
    public void testGetChildElements1() {
        Element element = new Element("name").addChild(new Element("name2"));
        Assert.assertEquals(element.getChildElements().size(), 1);
        Assert.assertEquals(element.getChildElements().get(0).getName(), "name2");
    }

    @Test
    public void testGetChildTexts() {
        Element element = new Element("name");
        Assert.assertEquals(element.getChildTexts().size(), 0);
    }

    @Test
    public void testGetChildTexts1() {
        Element element = new Element("name").addChild("text");
        Assert.assertEquals(element.getChildTexts().size(), 1);
        Assert.assertEquals(element.getChildTexts().get(0), "text");
    }

    @Test
    public void testGetChildTexts2() {
        Element element = new Element("name").addChild("text1").addChild("text2");
        Assert.assertEquals(element.getChildTexts().size(), 1);
        Assert.assertEquals(element.getChildTexts().get(0), "text1text2");
    }

    @Test
    public void testGetAttributes() {
        Element element = new Element("name").addAttribute("key");
        Assert.assertEquals(element.getAttributes().size(), 1);
        Assert.assertEquals(element.getAttributes().get("key"), "");
    }

    @Test
    public void testGetAttributes1() {
        Element element = new Element("name").addAttribute("key", "value");
        Assert.assertEquals(element.getAttributes().size(), 1);
        Assert.assertEquals(element.getAttributes().get("key"), "value");
    }

    @Test
    public void testGetAttributes2() {
        Element element = new Element("name").addAttribute("key", "value").addAttribute("key", "overwritten");
        Assert.assertEquals(element.getAttributes().size(), 1);
        Assert.assertEquals(element.getAttributes().get("key"), "overwritten");
    }

    @Test
    public void testAddChild4() {
        Element element = new Element("name").addChild(new Element("name2"), true);
        Assert.assertEquals(element.getChildElements().size(), 1);
        Assert.assertEquals(element.getChildElements().get(0).getName(), "name2");
    }

    @Test
    public void testToXml6() throws IOException {
        Element element = new Element("name").addChild(new Element("name2"), true);
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PrintWriter printWriter = new PrintWriter(byteArrayOutputStream)) {
            element.toXml(printWriter, 0);
            printWriter.flush();
            Assert.assertEquals(byteArrayOutputStream.toString(), "<name><name2 /></name>\n");
        }
    }
}
