package systemInformation.domain;

import java.net.URI;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.*;

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
}
