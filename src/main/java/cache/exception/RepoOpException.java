package cache.exception;

public class RepoOpException extends RuntimeException {
  public RepoOpException() {}

  public RepoOpException(String message) {
    super(message);
  }

  public RepoOpException(String message, Throwable cause) {
    super(message, cause);
  }
}
