package sheep.features.files;

import java.io.IOException;

/**
 * Thrown if a sheet file cannot be read.
 */
public class SheetFileReadingException extends IOException {
    /**
     * Construct a new exception without any additional details.
     */
    public SheetFileReadingException() {
        super();
    }

    /**
     * Construct a new exception with a description of the exception.
     * @param message The description of the exception.
     */
    public SheetFileReadingException(String message) {
        super(message);
    }

    /**
     * Construct a new exception with another exception as the base cause.
     * @param base The exception that caused this exception to be thrown.
     */
    public SheetFileReadingException(Exception base) {
        super(base);
    }


}
