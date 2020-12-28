package urlshortener.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import urlshortener.domain.ShortURL;

@Repository
public interface ShortURLRepository extends JpaRepository<ShortURL, String> {
  List<ShortURL> findByTarget(String target);

  ShortURL findByHash(String hash);
}
