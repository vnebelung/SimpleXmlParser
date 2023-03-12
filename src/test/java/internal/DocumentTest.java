package internal;

import main.Element;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class DocumentTest {

    @Test
    public void testToXml() throws IOException, URISyntaxException {
        Path tmp = Files.createTempFile("xml", null);
        Document document = new Document(Document.Type.XML);
        document.toXml(tmp);
        Assert.assertEquals(Files.readString(tmp),
                Files.readString(Paths.get(Objects.requireNonNull(getClass().getResource("/reference1.xml")).toURI())));
        Files.delete(tmp);
    }

    @Test
    public void testWriteToFile1() throws IOException, URISyntaxException {
        Path tmp = Files.createTempFile("xml", null);
        Document document = new Document(Document.Type.XML);
        document.addRoot(new Element("root"));
        document.toXml(tmp);
        Assert.assertEquals(Files.readString(tmp),
                Files.readString(Paths.get(Objects.requireNonNull(getClass().getResource("/reference2.xml")).toURI())));
        Files.delete(tmp);
    }

    @Test
    public void testAddRoot() {
        Document document = new Document(Document.Type.XML);
        Element root = new Element("root");
        document.addRoot(root);
        Assert.assertEquals(document.getRoot(), root);
    }

    @Test
    public void testGetRoot() {
        Document document = new Document(Document.Type.XML);
        Element root = new Element("root");
        document.addRoot(root);
        Assert.assertEquals(document.getRoot(), root);
    }

}
