package systemInformation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systemInformation.domain.Click;
import systemInformation.domain.ShortURL;

import java.util.List;

public interface ClickRepository extends JpaRepository<Click, Long> {

  List<Click> findByHash(ShortURL hash);
}
