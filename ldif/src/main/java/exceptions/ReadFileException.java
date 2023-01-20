package exceptions;

/**
 * Exception for errors read files.
 */
public class ReadFileException extends RuntimeException {
  public ReadFileException(String message) {
    super(message);
  }
}
