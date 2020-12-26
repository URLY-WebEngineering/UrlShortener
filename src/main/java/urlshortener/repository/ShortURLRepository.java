package urlshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import urlshortener.domain.ShortURL;
import urlshortener.repository.custom.ShortURLRepositoryCustom;

@Repository
public interface ShortURLRepository
    extends JpaRepository<ShortURL, String>, ShortURLRepositoryCustom {}
