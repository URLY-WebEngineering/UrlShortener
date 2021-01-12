package systemInformation.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import systemInformation.domain.ShortURL;

public interface ShortURLRepository extends JpaRepository<ShortURL, String> {

  ShortURL findByHash(String id);

  List<ShortURL> findByTarget(String target);
}
