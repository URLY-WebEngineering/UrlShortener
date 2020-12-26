package urlshortener.repository.custom;

import java.util.List;
import urlshortener.domain.ShortURL;

public interface ShortURLRepositoryCustom {
  List<ShortURL> findByTarget(String target);

  ShortURL findByHash(String hash);
}
