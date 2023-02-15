package main;

import java.io.PrintWriter;
import java.util.stream.IntStream;

class Text extends Child {

    private String text;

    public Text(String text) {
        super(Type.TEXT);
        this.text = text;
    }

    @Override
    void writeToFile(PrintWriter printWriter, int level) {
        IntStream.range(0, 2 * level).forEach(ignored -> printWriter.print(" "));
        if (level == 0) {
            printWriter.print(encode(text));
        } else {
            printWriter.println(encode(text));
        }
    }
}
