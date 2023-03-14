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
        Node child = new Node(Node.Type.ELEMENT, true) {
            @Override
            public void toXml(PrintWriter printWriter, int level) {
            }
        };
        Assert.assertEquals(child.getType(), Node.Type.ELEMENT);
    }

    @Test
    public void testGetType1() {
        Node child = new Node(Node.Type.TEXT, false) {
            @Override
            public void toXml(PrintWriter printWriter, int level) {
            }
        };
        Assert.assertEquals(child.getType(), Node.Type.TEXT);
    }

    @Test
    public void testIsIndented1() {
        Node child = new Node(Node.Type.ELEMENT, true) {
            @Override
            public void toXml(PrintWriter printWriter, int level) {
            }
        };
        Assert.assertTrue(child.isIndented());
    }

    @Test
    public void testIsIndented2() {
        Node child = new Node(Node.Type.ELEMENT, false) {
            @Override
            public void toXml(PrintWriter printWriter, int level) {
            }
        };
        Assert.assertFalse(child.isIndented());
    }

    @Test
    public void testSetIndented() {
        Node child = new Node(Node.Type.ELEMENT, false) {
            @Override
            public void toXml(PrintWriter printWriter, int level) {
            }
        };
        child.setIndented(true);
        Assert.assertTrue(child.isIndented());
    }

    @Test
    public void testSetIndented2() {
        Node child = new Node(Node.Type.ELEMENT, true) {
            @Override
            public void toXml(PrintWriter printWriter, int level) {
            }
        };
        child.setIndented(false);
        Assert.assertFalse(child.isIndented());
    }

}
