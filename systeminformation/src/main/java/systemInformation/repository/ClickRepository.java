package systemInformation.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import systemInformation.domain.Click;
import systemInformation.domain.ShortURL;

public interface ClickRepository extends JpaRepository<Click, Long> {

  List<Click> findByHash(ShortURL hash);

  List<Click> deleteAllByHash(ShortURL hash);
}
