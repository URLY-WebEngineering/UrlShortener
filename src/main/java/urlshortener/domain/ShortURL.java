package urlshortener.domain;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.sql.Date;
import urlshortener.web.UrlShortenerController;

public class ShortURL {

  private String hash;
  private String target;
  private URI uri;
  private String sponsor;
  private Date created;
  private String owner;
  private Integer mode;
  private Boolean safe;
  private String ip;
  private String country;
  private URI qr;

  public ShortURL(
      String hash,
      String target,
      URI uri,
      String sponsor,
      Date created,
      String owner,
      Integer mode,
      Boolean safe,
      String ip,
      String country,
      URI qr) {
    this.hash = hash;
    this.target = target;
    this.uri = uri;
    this.sponsor = sponsor;
    this.created = created;
    this.owner = owner;
    this.mode = mode;
    this.safe = safe;
    this.ip = ip;
    this.country = country;
    this.qr = qr;
  }

  public ShortURL() {}

  public String getHash() {
    return hash;
  }

  public String getTarget() {
    return target;
  }

  public URI getUri() {
    // I will not be necessary to save the uri on the database
    // Perhaps it is better to generate it when it is needed
    uri = linkTo(methodOn(UrlShortenerController.class).redirectTo(hash, null)).toUri();
    return uri;
  }

  public Date getCreated() {
    return created;
  }

  public String getOwner() {
    return owner;
  }

  public Integer getMode() {
    return mode;
  }

  public String getSponsor() {
    return sponsor;
  }

  public Boolean getSafe() {
    return safe;
  }

  public String getIP() {
    return ip;
  }

  public String getCountry() {
    return country;
  }

  public URI getQr() {
    return qr;
  }

  public void setQr(URI qr) {
    this.qr = qr;
  }
}
