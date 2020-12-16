package urlshortener.service.exceptions;

public class BadCustomBackhalfException extends Exception {

  public BadCustomBackhalfException(String errorMessage) {
    super(errorMessage);
  }
}
