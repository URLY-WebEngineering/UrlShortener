package urlshortener.domain;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.*;
import urlshortener.web.UrlShortenerController;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "ShortURL")
public class ShortURL {

  @Id private String hash;

  private String target;
  private URI uri;
  private String sponsor;
  private Date created;
  private String owner;
  private Integer mode;

  private Boolean safe;
  private String IP;
  private String country;
  private URI qr;
  private Boolean reachable;
  private Boolean checked;

  public URI getUri() {
    // I will not be necessary to save the uri on the database
    // Perhaps it is better to generate it when it is needed
    uri = linkTo(methodOn(UrlShortenerController.class).redirectTo(hash, null)).toUri();
    return uri;
  }
}
