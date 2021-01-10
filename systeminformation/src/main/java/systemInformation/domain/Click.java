package systemInformation.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Click {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hash", nullable = false)
  private systemInformation.domain.ShortURL hash;

  private Date created;
  private String referrer;
  private String browser;
  private String platform;
  private String ip;
  private String country;
}
