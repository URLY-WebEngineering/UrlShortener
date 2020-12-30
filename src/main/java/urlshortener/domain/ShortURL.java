package urlshortener.domain;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.*;
import urlshortener.web.UrlShortenerController;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ShortURL {

  @Id @Getter @Setter private String hash;

  @Getter @Setter private String target;
  @Setter private URI uri;
  @Getter @Setter private String sponsor;
  @Getter @Setter private Date created;
  @Getter @Setter private String owner;
  @Getter @Setter private Integer mode;
  @Getter @Setter private Boolean safe;
  @Getter @Setter private String ip;
  @Getter @Setter private String country;
  @Getter @Setter private URI qr;
  @Getter @Setter private Boolean reachable;
  @Getter @Setter private Boolean checked;

  public URI getUri() {
    // I will not be necessary to save the uri on the database
    // Perhaps it is better to generate it when it is needed
    uri = linkTo(methodOn(UrlShortenerController.class).redirectTo(hash, null)).toUri();
    return uri;
  }
}
