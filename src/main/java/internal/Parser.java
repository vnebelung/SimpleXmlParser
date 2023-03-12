package internal;

import main.Document;
import main.Element;
import main.ParseException;
import main.SimpleXmlParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EmptyStackException;
import java.util.Stack;

public class Parser {

    private int current, column, line, backupCurrent, backupColumn, backupLine;
    private BufferedReader bufferedReader;
    private Stack<Element> elements = new Stack<>();

    private enum State {
        AFTER_FIRST_LINE,
        AFTER_ELEMENT_OPEN_NAME,
        ATTRIBUTE_OR_SHORT_CLOSE,
        END,
        DECLARATION_OR_DOCTYPE,
        ELEMENT_OPEN_START,
        CANDIDATE_END,
        ELEMENT_START_OR_TEXT,
        ELEMENT_START,
        ELEMENT_OPEN_NAME,
        ELEMENT_CLOSE_NAME,
        AFTER_ELEMENT,
        ELEMENT_END,
        START
    }

    public Document parse(Path file) throws ParseException {
        Document result = null;
        try (BufferedReader bufferedReader = Files.newBufferedReader(file)) {
            this.bufferedReader = bufferedReader;
            column = 0;
            line = 1;
            elements.clear();
            State state = State.START;
            read();
            while (state != State.END) {
                switch (state) {
                    case START -> {
                        parseWhitespace(true);
                        state = State.DECLARATION_OR_DOCTYPE;
                    }
                    case DECLARATION_OR_DOCTYPE -> {
                        parseLeftAngleBracket(false);
                        if (parseDeclaration()) {
                            parseRightAngleBracket();
                            result = SimpleXmlParser.createXml();
                            state = State.AFTER_FIRST_LINE;
                            continue;
                        }
                        if (parseDoctype()) {
                            parseRightAngleBracket();
                            result = SimpleXmlParser.createHtml();
                            state = State.AFTER_FIRST_LINE;
                            continue;
                        }
                        throw new ParseException("Missing valid XML declaration or HTML doctype", line, column);
                    }
                    case AFTER_FIRST_LINE -> {
                        parseWhitespace(true);
                        state = State.ELEMENT_OPEN_START;
                    }
                    case ELEMENT_OPEN_START -> {
                        parseLeftAngleBracket(false);
                        state = State.ELEMENT_OPEN_NAME;
                    }
                    case ELEMENT_OPEN_NAME -> {
                        String name = parseName();
                        Element element = new Element(name);
                        if (elements.isEmpty()) {
                            result.addRoot(element);
                        } else {
                            elements.peek().addChild(element);
                        }
                        elements.push(element);
                        state = State.AFTER_ELEMENT_OPEN_NAME;
                    }
                    case AFTER_ELEMENT_OPEN_NAME ->
                            state = parseWhitespace(true) ? State.ATTRIBUTE_OR_SHORT_CLOSE : State.ELEMENT_END;
                    case ELEMENT_END -> {
                        parseRightAngleBracket();
                        state = State.AFTER_ELEMENT;
                    }
                    case ATTRIBUTE_OR_SHORT_CLOSE -> {
                        if (parseSlash()) {
                            elements.pop();
                            state = State.ELEMENT_END;
                            continue;
                        }
                        String key = parseName();
                        if (parseEqualSign()) {
                            parseQuotationMark();
                            String value = parseName();
                            parseQuotationMark();
                            elements.peek().addAttribute(key, value);
                        } else {
                            elements.peek().addAttribute(key);
                        }
                        state = State.AFTER_ELEMENT_OPEN_NAME;
                    }
                    case AFTER_ELEMENT -> {
                        parseWhitespace(true);
                        state = elements.isEmpty() ? State.CANDIDATE_END : State.ELEMENT_START_OR_TEXT;
                    }
                    case CANDIDATE_END -> {
                        parseEnd();
                        state = State.END;
                    }
                    case ELEMENT_START_OR_TEXT -> {
                        if (parseLeftAngleBracket(true)) {
                            state = State.ELEMENT_START;
                            continue;
                        }
                        String text = parseText();
                        elements.peek().addChild(text);
                        parseLeftAngleBracket(false);
                        state = State.ELEMENT_START;
                    }
                    case ELEMENT_START -> {
                        if (parseSlash()) {
                            state = State.ELEMENT_CLOSE_NAME;
                            continue;
                        }
                        state = State.ELEMENT_OPEN_NAME;
                    }
                    case ELEMENT_CLOSE_NAME -> {
                        parseName(elements.peek().getName());
                        elements.pop();
                        state = State.ELEMENT_END;
                    }
                }
            }
        } catch (EmptyStackException | IOException e) {
            throw new ParseException(e);
        }
        return result;
    }

    private String parseText() throws IOException {
        StringBuilder result = new StringBuilder();
        while (current != '<') {
            result.append(Character.toString(current));
            read();
        }
        return result.toString().trim();
    }

    private void parseEnd() throws IOException, ParseException {
        if (current < 0) {
            return;
        }
        throw new ParseException(
                String.format("Unexpected character '%s' instead of end of stream", Character.toString(current)), line,
                column);
    }

    private void mark() throws IOException {
        bufferedReader.mark(0);
        backupCurrent = current;
        backupColumn = column;
        backupLine = line;
    }

    private void reset() throws IOException {
        bufferedReader.reset();
        current = backupCurrent;
        column = backupColumn;
        line = backupLine;
    }

    private void read() throws IOException {
        int character = bufferedReader.read();
        column++;
        current = character;
        if (current == '\n') {
            line++;
            column = 0;
        }
    }

    private boolean parseEqualSign() throws IOException {
        if (current == '=') {
            read();
            return true;
        }
        return false;
    }

    private void parseQuotationMark() throws IOException, ParseException {
        if (current == '"') {
            read();
            return;
        }
        throw new ParseException(
                String.format("Unexpected character '%s' instead of '\"'", Character.toString(current)), line, column);
    }

    private String parseName() throws ParseException, IOException {
        StringBuilder result = new StringBuilder();
        if (!isStartCharacter(current)) {
            throw new ParseException(
                    String.format("Unexpected character '%s' for element name", Character.toString(current)), line,
                    column);
        }
        result.append(Character.toString(current));
        read();
        while (isCharacter(current)) {
            result.append(Character.toString(current));
            read();
        }
        return result.toString();
    }

    private void parseName(String reference) throws ParseException, IOException {
        String name = parseName();
        if (!name.equals(reference)) {
            throw new ParseException(String.format("Unexpected element name '%s' instead of '%s'", name, reference),
                    line, column - name.length());
        }
    }

    public static boolean isStartCharacter(int c) {
        // :
        if (c == ':') {
            return true;
        }
        // A-Z
        if (0x41 <= c && c <= 0x5a) {
            return true;
        }
        // _
        if (c == '_') {
            return true;
        }
        // a-z
        if (0x61 <= c && c <= 0x7a) {
            return true;
        }
        if (0xC0 <= c && c <= 0xD6) {
            return true;
        }
        if (0xD8 <= c && c <= 0xF6) {
            return true;
        }
        if (0xF8 <= c && c <= 0x2FF) {
            return true;
        }
        if (0x370 <= c && c <= 0x37D) {
            return true;
        }
        if (0x37F <= c && c <= 0x1FFF) {
            return true;
        }
        if (0x200C <= c && c <= 0x200D) {
            return true;
        }
        if (0x2070 <= c && c <= 0x218F) {
            return true;
        }
        if (0x2C00 <= c && c <= 0x2FEF) {
            return true;
        }
        if (0x3001 <= c && c <= 0xD7FF) {
            return true;
        }
        if (0xF900 <= c && c <= 0xFDCF) {
            return true;
        }
        if (0xFDF0 <= c && c <= 0xFFFD) {
            return true;
        }
        return 0x010000 <= c && c <= 0x0EFFFF;
    }

    public static boolean isCharacter(int c) {
        if (c == '-') {
            return true;
        }
        if (c == '.') {
            return true;
        }
        if (0x30 <= c && c <= 0x39) {
            return true;
        }
        if (c == 0xb7) {
            return true;
        }
        if (0x0300 <= c && c <= 0x036f) {
            return true;
        }
        if (0x203f <= c && c <= 0x2040) {
            return true;
        }
        return isStartCharacter(c);
    }

    private boolean parseDeclaration() throws IOException, ParseException {
        mark();
        if (current != '?') {
            reset();
            return false;
        }
        read();
        parseString("xml");
        parseWhitespace(false);
        parseString("version=\"1.0\"");
        parseWhitespace(false);
        parseString("?");
        return true;
    }

    private void parseString(String string) throws IOException, ParseException {
        for (char c : string.toCharArray()) {
            if (c != current) {
                throw new ParseException(String.format("Unexpected character '%s' instead of '%s'", current, c), line,
                        column);
            }
            read();
        }
    }

    private boolean parseDoctype() throws IOException, ParseException {
        mark();
        if (current != '!') {
            reset();
            return false;
        }
        read();
        parseString("DOCTYPE");
        parseWhitespace(false);
        parseString("html");
        return true;
    }

    private boolean parseLeftAngleBracket(boolean optional) throws IOException, ParseException {
        if (current == '<') {
            read();
            return true;
        }
        if (optional) {
            return false;
        }
        throw new ParseException(String.format("Unexpected character '%s' instead of '<'", Character.toString(current)),
                line, column);
    }

    private void parseRightAngleBracket() throws ParseException, IOException {
        if (current == '>') {
            read();
            return;
        }
        throw new ParseException(String.format("Unexpected character '%s' instead of '>'", Character.toString(current)),
                line, column);
    }

    private boolean parseSlash() throws IOException {
        if (current == '/') {
            read();
            return true;
        }
        return false;
    }

    private boolean parseWhitespace(boolean optional) throws IOException, ParseException {
        boolean result = false;
        while (isWhitespaceCharacter(current)) {
            read();
            result = true;
        }
        if (optional) {
            return result;
        }
        if (result) {
            return true;
        }
        throw new ParseException(String.format("Unexpected character '%s' instead of ' '", Character.toString(current)),
                line, column);
    }

    private boolean isWhitespaceCharacter(int current) {
        if (current == 0x20) {
            return true;
        }
        if (current == 0x09) {
            return true;
        }
        if (current == 0x0d) {
            return true;
        }
        return current == 0x0a;
    }
}
