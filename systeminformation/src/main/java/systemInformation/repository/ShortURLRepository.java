package systemInformation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systemInformation.domain.ShortURL;

public interface ShortURLRepository extends JpaRepository<ShortURL, String> {}
