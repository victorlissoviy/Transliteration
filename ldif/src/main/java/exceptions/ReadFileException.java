package exceptions;

public class ReadFileException extends RuntimeException {
  public ReadFileException(String message) {
    super(message);
  }
}
