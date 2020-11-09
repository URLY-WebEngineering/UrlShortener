package urlshortener.domain;

import java.sql.Date;

public class Click {

  private final Long id;
  private final String hash;
  private final Date created;
  private final String referrer;
  private final String browser;
  private final String platform;
  private final String ip;
  private final String country;

  public Click(
      Long id,
      String hash,
      Date created,
      String referrer,
      String browser,
      String platform,
      String ip,
      String country) {
    this.id = id;
    this.hash = hash;
    this.created = created;
    this.referrer = referrer;
    this.browser = browser;
    this.platform = platform;
    this.ip = ip;
    this.country = country;
  }

  public Long getId() {
    return id;
  }

  public String getHash() {
    return hash;
  }

  public Date getCreated() {
    return created;
  }

  public String getReferrer() {
    return referrer;
  }

  public String getBrowser() {
    return browser;
  }

  public String getPlatform() {
    return platform;
  }

  public String getIp() {
    return ip;
  }

  public String getCountry() {
    return country;
  }
}
