package urlshortener.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import urlshortener.domain.Click;

// Repository extends from the JpaRepository. It provides the type of the entity and of its primary
// key.
@Repository
public interface ClickRepository extends JpaRepository<Click, Long> {
  public List<Click> findByHash(String hash);
}
