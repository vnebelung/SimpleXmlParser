package internal;

import java.io.PrintWriter;
import java.util.stream.IntStream;

public class Text extends Node {

    public String getText() {
        return text;
    }

    private String text;

    public Text(String text) {
        super(Type.TEXT);
        this.text = text;
    }

    @Override
    public void toXml(PrintWriter printWriter, int level) {
        IntStream.range(0, 2 * level).forEach(ignored -> printWriter.print(" "));
        if (level == 0) {
            printWriter.print(encode(text));
        } else {
            printWriter.println(encode(text));
        }
    }
}
