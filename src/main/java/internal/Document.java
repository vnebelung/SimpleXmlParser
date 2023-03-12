package internal;

import main.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

public class Document implements main.Document {

    public final static String HTML_DOCTYPE = "<!DOCTYPE html>";
    public final static String XML_DOCTYPE = "<?xml version=\"1.0\" ?>";
    private Element root;
    private String declaration;

    public enum Type {
        HTML,
        XML
    }

    public Document(Type type) {
        declaration = switch (type) {
            case HTML -> HTML_DOCTYPE;
            case XML -> XML_DOCTYPE;
        };
    }

    @Override
    public void addRoot(Element element) {
        root = element;
    }

    @Override
    public void toXml(Path file) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(file.toFile()))) {
            printWriter.println(declaration);
            if (root != null) {
                root.toXml(printWriter, 0);
            }
        }
    }

    @Override
    public Element getRoot() {
        return root;
    }
}
