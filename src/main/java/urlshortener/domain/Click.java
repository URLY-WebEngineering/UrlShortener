package urlshortener.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "click")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Click {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "hash", nullable = false)
  private String hash;

  @Column(name = "created")
  private Date created;

  @Column(name = "referrer")
  private String referrer;

  @Column(name = "browser")
  private String browser;

  @Column(name = "platform")
  private String platform;

  @Column(name = "ip")
  private String ip;

  @Column(name = "country")
  private String country;
}
