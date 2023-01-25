package exceptions;

public class WriteFileException extends RuntimeException {
  public WriteFileException(String message) {
    super(message);
  }
}
