package internal;

import java.io.PrintWriter;

public abstract class Node {

    private Type type;
    private boolean indented;

    public Node(Type type, boolean indented) {
        this.type = type;
        this.indented = indented;
    }

    public boolean isIndented() {
        return indented;
    }

    public void setIndented(boolean indented) {
        this.indented = indented;
    }

    protected enum Type {
        TEXT,
        ELEMENT
    }

    public abstract void toXml(PrintWriter printWriter, int level);

    protected static String encode(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // encode standard ASCII characters into HTML entities where needed
            if (c < '\200') {
                switch (c) {
                    case '"' -> result.append("&quot;");
                    case '&' -> result.append("&amp;");
                    case '<' -> result.append("&lt;");
                    case '>' -> result.append("&gt;");
                    default -> result.append(c);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public Type getType() {
        return type;
    }

}
