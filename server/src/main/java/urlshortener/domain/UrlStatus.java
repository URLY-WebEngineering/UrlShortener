package urlshortener.domain;

public enum UrlStatus {
  OK("OK"),
  UNSAFE("Unsafe url"),
  INVALID("Invalid url"),
  UNREACHABLE("Unreachable url"),
  CHECKING("Url is not validated yet");

  private String status;

  UrlStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
