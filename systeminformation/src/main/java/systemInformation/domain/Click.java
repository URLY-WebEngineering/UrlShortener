package systemInformation.domain;

import java.sql.Date;
import javax.persistence.*;
import lombok.*;

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
