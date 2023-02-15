package main;

import java.io.PrintWriter;

abstract class Child {

    abstract void writeToFile(PrintWriter printWriter, int level);

    String encode(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // encode standard ASCII characters into HTML entities where needed
            if (c < '\200') {
                switch (c) {
                    case '"' -> result.append("&quot;");
                    case '&' -> result.append("&amp;");
                    case '<' -> result.append("&lt;");
                    case '>' -> result.append("&gt;");
                    default -> result.append(c);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }



}
