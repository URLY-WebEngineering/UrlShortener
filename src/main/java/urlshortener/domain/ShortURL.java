package urlshortener.domain;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.*;
import urlshortener.web.UrlShortenerController;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ShortURL")
@Data
@Table(name = "ShortURL")
public class ShortURL {

  @Id private String hash;

  @Column(name = "target")
  private String target;

  @Column(name = "uri")
  private URI uri;

  @Column(name = "sponsor")
  private String sponsor;

  @Column(name = "created")
  private Date created;

  @Column(name = "owner")
  private String owner;

  @Column(name = "mode")
  private Integer mode;

  @Column(name = "safe")
  private Boolean safe;

  @Column(name = "IP")
  private String IP;

  @Column(name = "country")
  private String country;

  @Column(name = "qr")
  private URI qr;

  @Column(name = "reachable")
  private Boolean reachable;

  @Column(name = "checked")
  private Boolean checked;

  public URI getUri() {
    // I will not be necessary to save the uri on the database
    // Perhaps it is better to generate it when it is needed
    uri = linkTo(methodOn(UrlShortenerController.class).redirectTo(hash, null)).toUri();
    return uri;
  }
}
