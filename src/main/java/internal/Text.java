package internal;

import java.io.PrintWriter;
import java.util.stream.IntStream;

public class Text extends Node {

    public String getText() {
        return text;
    }

    private String text;

    public Text(String text) {
        super(Type.TEXT, false);
        this.text = text;
    }

    @Override
    public void toXml(PrintWriter printWriter, int level) {
        if (isIndented()) {
            IntStream.range(0, 2 * level).forEach(ignored -> printWriter.print(" "));
            printWriter.println(encode(text));
        } else {
            printWriter.print(encode(text));
        }
    }
}
