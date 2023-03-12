package main;

import java.io.IOException;
import java.nio.file.Path;

public interface Document {

    void addRoot(Element element);

    void toXml(Path file) throws IOException;

    Element getRoot();
}
