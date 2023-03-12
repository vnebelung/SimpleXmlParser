package main;

public class ParseException extends Exception {

    /**
     * Constructs a ParseException with the specified detail message and ine/column offset.
     * A detail message is a String that describes this particular exception.
     *
     * @param s      the detail message
     * @param line   the line where the error is found while parsing.
     * @param column the column position where the error is found while parsing.
     */
    public ParseException(String s, int line, int column) {
        super(String.format("Error in line %d at column %d: %s", line, column, s));
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of {@code (cause==null ? null : cause
     * .toString())} (which typically contains the class and detail message of {@code cause}).
     * This constructor is useful for exceptions that are little more than wrappers for other throwables (for
     * example, {@link java.security.PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).  (A {@code
     *              null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ParseException(Exception cause) {
        super(cause);
    }

}
