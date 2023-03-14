package internal;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class TextTest {

    @Test
    public void testToXml() throws IOException {
        Text text = new Text("TEXT");
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); PrintWriter printWriter =
                new PrintWriter(byteArrayOutputStream)) {
            text.toXml(printWriter, 0);
            printWriter.flush();
            Assert.assertEquals(byteArrayOutputStream.toString(), "TEXT");
        }
    }

    @Test
    public void testToXml1() throws IOException {
        Text text = new Text("TEXT");
        text.setIndented(true);
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); PrintWriter printWriter =
                new PrintWriter(byteArrayOutputStream)) {
            text.toXml(printWriter, 2);
            printWriter.flush();
            Assert.assertEquals(byteArrayOutputStream.toString(), "    TEXT\n");
        }
    }

    @Test
    public void testGetText() {
        Text text = new Text("text");
        Assert.assertEquals(text.getText(), "text");
    }
}
