package urlshortener.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import urlshortener.domain.ShortURL;

public interface ShortURLRepository extends JpaRepository<ShortURL, String> {

  ShortURL findByHash(String id);

  List<ShortURL> findByTarget(String target);

}
