package urlshortener.repository.custom;

import java.util.List;
import urlshortener.domain.Click;

public interface ClickRepositoryCustom {
  public List<Click> findByHash(String hash);
}
