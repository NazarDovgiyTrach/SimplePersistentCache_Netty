package cache.exception;

public class KeyExistsException extends RuntimeException {
  public KeyExistsException(String message) {
    super(message);
  }

  public KeyExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
