package main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

public class Document {

    private Element root;
    private String declaration;

    enum Type {
        HTML,
        XML
    }

    Document(Type type) {
        declaration = switch (type) {
            case HTML -> "<!DOCTYPE html>";
            case XML -> "<?xml version=\"1.0\"?>";
        };
    }

    public Document addRoot(Element element) {
        root = element;
        return this;
    }

    public void writeToFile(Path file) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(file.toFile()))) {
            printWriter.println(declaration);
            root.writeToFile(printWriter, 0);
        }
    }
}
