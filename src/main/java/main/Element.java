package main;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Element extends Child {

    private String name;
    private Map<String, String> attributes = new HashMap<>();
    private List<Child> children = new LinkedList<>();

    public Element(String name) {
        this.name = name;
    }

    public Element(String name, String text) {
        this.name = name;
        addChild(text);
    }

    public Element addAttribute(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public Element addAttribute(String key) {
        attributes.put(key, "");
        return this;
    }

    public Element addChild(Element element) {
        children.add(element);
        return this;
    }

    public Element addChild(String text) {
        children.add(new Text(text));
        return this;
    }

    @Override
    void writeToFile(PrintWriter printWriter, int level) {
        IntStream.range(0, 2 * level).forEach(ignored -> printWriter.print(" "));
        printWriter.print("<");
        printWriter.print(encode(name));
        for (Map.Entry<String, String> attribute : attributes.entrySet()) {
            printWriter.print(" ");
            printWriter.print(encode(attribute.getKey()));
            if (!attribute.getValue().isEmpty()) {
                printWriter.print("=\"");
                printWriter.print(encode(attribute.getValue()));
                printWriter.print("\"");
            }
        }
        printWriter.println(">");
        children.forEach(child -> child.writeToFile(printWriter, level + 1));
        printWriter.print("</");
        printWriter.print(encode(name));
        printWriter.println(">");
    }

}