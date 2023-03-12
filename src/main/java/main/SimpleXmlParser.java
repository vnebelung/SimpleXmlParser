package main;

import internal.Parser;

import java.nio.file.Path;

/**
 * This class represents the XML parser, which handles the document structure that can be exported in an XML file.
 * Additionally an XML file can be parsed so that an element tree representing the XML structure is returned.
 */
public class SimpleXmlParser {

    private static Parser parser = new Parser();

    private SimpleXmlParser() {
    }

    /**
     * Creates an empty HTML document containing only an HTML doctype. This document can be filled with a root
     * element containing other nodes.
     *
     * @return an empty HTML document
     */
    public static Document createHtml() {
        return new internal.Document(internal.Document.Type.HTML);
    }

    /**
     * Creates an empty XML document containing only an XML declaration. This document can be filled with a root
     * element containing other nodes.
     *
     * @return an empty XML document
     */
    public static Document createXml() {
        return new internal.Document(internal.Document.Type.XML);
    }

    /**
     * Parses an XML structure contained in the given file. The XML structure will be transformed in an element tree.
     *
     * @param file the XML file
     * @return the parsed XML document
     * @throws ParseException if the file contains an invalid XML structure.
     */
    public static Document parse(Path file) throws ParseException {
        return parser.parse(file);
    }

}
