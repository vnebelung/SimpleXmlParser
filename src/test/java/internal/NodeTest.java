package internal;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.PrintWriter;

public class NodeTest {

    @Test
    public void testEncode() {
        String input = "TEXT\"TEXT&TEXT<TEXT>TEXT\"TEXT&TEXT<TEXT>Ü";
        Assert.assertEquals(Node.encode(input),
                "TEXT&quot;TEXT&amp;TEXT&lt;TEXT&gt;TEXT&quot;TEXT&amp;TEXT&lt;TEXT&gt;Ü");
    }

    @Test
    public void testGetType() {
        Node child = new Node(Node.Type.ELEMENT) {
            @Override
            public void toXml(PrintWriter printWriter, int level) {
            }
        };
        Assert.assertEquals(child.getType(), Node.Type.ELEMENT);
    }

    @Test
    public void testGetType1() {
        Node child = new Node(Node.Type.TEXT) {
            @Override
            public void toXml(PrintWriter printWriter, int level) {
            }
        };
        Assert.assertEquals(child.getType(), Node.Type.TEXT);
    }
}
