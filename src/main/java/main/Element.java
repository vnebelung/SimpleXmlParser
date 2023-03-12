package main;

import internal.Node;
import internal.Parser;
import internal.Text;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

/**
 * This class represents an XML element which can added as a root node to a document or as a child element to another
 * element. The element's constructor requires a name that will be the XML element's name. An element can have
 * optional attributes and optional child nodes, which can be other elements or text nodes.
 */
public class Element extends Node {

    private String name;
    private Map<String, String> attributes = new TreeMap<>();
    private List<Node> children = new LinkedList<>();
    private List<Element> childElements = new LinkedList<>();
    private List<Text> childTexts = new LinkedList<>();
    private boolean preventIndent;

    /**
     * Creates an element with a given name.
     *
     * @param name the element's name
     * @throws IllegalArgumentException if the name contains characters that do not match the requirements of XML names
     */
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

    /**
     * Returns the name of the element.
     *
     * @return the element's name
     */
    public String getName() {
        return name;
    }

    /**
     * Creates an element with a given name and a child text node with a given text.
     *
     * @param name the element's name
     * @param text the text of the element's child text node
     */
    public Element(String name, String text) {
        this(name);
        addChild(text);
    }

    /**
     * Adds an attribute to the element with a given key and value. If the element previously contained an attribute for
     * the given key, the old value is replaced by the specified value.
     *
     * @param key   the attribute's key
     * @param value the attribute's value
     * @return the modified element
     * @throws IllegalArgumentException if the key contains characters that do not match the requirements of XML names
     */
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

    /**
     * Adds an attribute to the element with a given key and an empty value. If the element previously contained an
     * attribute for the given key, the old value is replaced by an empty value.
     *
     * @param key the attribute's key
     * @return the modified element
     */
    public Element addAttribute(String key) {
        return addAttribute(key, "");
    }

    /**
     * Adds the given element as a child to this element. The child element is added after already existing child
     * elements. When exporting the element tree as XML, the element will be indented, which will add whitespace
     * before and after the element when interpreted as HTML.
     *
     * @param element the child element
     * @return the modified element
     */
    public Element addChild(Element element) {
        return addChild(element, false);
    }

    /**
     * Adds the given element as a child to this element. The child element is added after already existing child
     * elements. If preventIndent is set to true, the element will not be indented when exporting the element tree as
     * XML, which prevents whitespace before and after the element when interpreted as HTML.
     *
     * @param element       the child element
     * @param preventIndent true, if the given element shall not be intended
     * @return the modified element
     */
    public Element addChild(Element element, boolean preventIndent) {
        element.preventIndent = preventIndent;
        children.add(element);
        childElements.add(element);
        return this;
    }

    /**
     * Adds the given text as a child to this element. The text is added after already existing child
     * elements. If the last child of this element is already a text node, the given text will be appended to this
     * text node. When exporting the element tree as XML and the text child node is the only child of this element,
     * the text node will not be indented, which prevents whitespace before and after the element when interpreted as
     * HTML.
     *
     * @param text the child element
     * @return the modified element
     */
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

    /**
     * Prints out the element and all its child nodes as an XML formatted string into the given PrintWriter. The
     * element is intended by the given level * 2 space characters.
     *
     * @param printWriter the PrintWriter the XML string is written to
     * @param level       the level of intend
     */
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

    /**
     * Returns all child nodes of the type ELEMENT in the order they appear in the element's child list. This means
     * that all child text nodes are not part of the returned list.
     *
     * @return the list of all child elements
     */
    public List<Element> getChildElements() {
        return Collections.unmodifiableList(childElements);
    }

    /**
     * Returns all child nodes of the type TEXT in the order they appear in the element's child list. This means
     * that all child element nodes are not part of the returned list.
     *
     * @return the list of all child text nodes
     */
    public List<String> getChildTexts() {
        return childTexts.stream().map(Text::getText).toList();
    }

    /**
     * Returns all of the element's attributes in alphabetical key order.
     *
     * @return the key-value map of all attributes
     */
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

}
