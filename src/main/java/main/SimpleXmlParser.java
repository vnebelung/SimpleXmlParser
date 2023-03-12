package main;

import internal.Parser;

import java.nio.file.Path;

public class SimpleXmlParser {

    private static Parser parser = new Parser();

    private SimpleXmlParser() {
    }

    public static Document createHtml() {
        return new internal.Document(internal.Document.Type.HTML);
    }

    public static Document createXml() {
        return new internal.Document(internal.Document.Type.XML);
    }

    public static Document parse(Path file) throws ParseException {
        return parser.parse(file);
    }

}
