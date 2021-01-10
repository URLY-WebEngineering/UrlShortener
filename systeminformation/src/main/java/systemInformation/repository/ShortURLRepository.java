package systemInformation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systemInformation.domain.ShortURL;

import java.util.List;

public interface ShortURLRepository extends JpaRepository<ShortURL, String> {

  ShortURL findByHash(String id);

  List<ShortURL> findByTarget(String target);
}
