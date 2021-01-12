package urlshortener.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import urlshortener.domain.Click;
import urlshortener.domain.ShortURL;

public interface ClickRepository extends JpaRepository<Click, Long> {

  List<Click> findByHash(ShortURL hash);

  List<Click> deleteAllByHash(ShortURL hash);
}
