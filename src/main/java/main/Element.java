package main;

import internal.Node;
import internal.Parser;
import internal.Text;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

public class Element extends Node {

    private String name;
    private Map<String, String> attributes = new TreeMap<>();
    private List<Node> children = new LinkedList<>();
    private List<Element> childElements = new LinkedList<>();
    private List<Text> childTexts = new LinkedList<>();
    private boolean preventIndent;

    public Element(String name) {
        super(Type.ELEMENT);
        if (!Parser.isStartCharacter(name.charAt(0))) {
            throw new IllegalArgumentException("Illegal character in element name");
        }
        if (name.chars().anyMatch(c -> !Parser.isCharacter(c))) {
            throw new IllegalArgumentException("Illegal character in element name");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Element(String name, String text) {
        this(name);
        addChild(text);
    }

    public Element addAttribute(String key, String value) {
        if (!Parser.isStartCharacter(key.charAt(0))) {
            throw new IllegalArgumentException("Illegal character in attribute key");
        }
        if (key.chars().anyMatch(c -> !Parser.isCharacter(c))) {
            throw new IllegalArgumentException("Illegal character in attribute key");
        }
        attributes.put(key, value);
        return this;
    }

    public Element addAttribute(String key) {
        return addAttribute(key, "");
    }

    public Element addChild(Element element) {
        return addChild(element, false);
    }

    public Element addChild(Element element, boolean preventIndent) {
        element.preventIndent = preventIndent;
        children.add(element);
        childElements.add(element);
        return this;
    }

    public Element addChild(String text) {
        if (text.isBlank()) {
            throw new IllegalArgumentException("Text must not be blank");
        }
        if (!children.isEmpty() && children.get(children.size() - 1).getType() == Type.TEXT) {
            Text old = childTexts.remove(childTexts.size() - 1);
            children.remove(old);
            addChild(old.getText() + text);
        } else {
            Text child = new Text(text);
            children.add(child);
            childTexts.add(child);
        }
        return this;
    }

    @Override
    public void toXml(PrintWriter printWriter, int level) {
        if (!preventIndent) {
            IntStream.range(0, 2 * level).forEach(ignored -> printWriter.print(" "));
        }
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
        if (children.isEmpty()) {
            if (preventIndent) {
                printWriter.print(" />");
            } else {
                printWriter.println(" />");
            }
        } else {
            int childLevel = (children.size() == 1 && children.get(0).getType() == Type.TEXT) ||
                    (children.get(0).getType() == Type.ELEMENT && childElements.get(0).preventIndent) ? 0 : level + 1;
            if (childLevel != 0) {
                printWriter.println(">");
            } else {
                printWriter.print(">");
            }
            children.forEach(child -> child.toXml(printWriter, childLevel));
            if (childLevel != 0) {
                IntStream.range(0, 2 * level).forEach(ignored -> printWriter.print(" "));
            }
            printWriter.print("</");
            printWriter.print(encode(name));
            printWriter.println(">");
        }
    }

    public List<Element> getChildElements() {
        return Collections.unmodifiableList(childElements);
    }

    public List<String> getChildTexts() {
        return childTexts.stream().map(Text::getText).toList();
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

}
