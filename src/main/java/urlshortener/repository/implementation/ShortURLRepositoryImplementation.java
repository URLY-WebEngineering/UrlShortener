package urlshortener.repository.implementation;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import urlshortener.domain.ShortURL;
import urlshortener.repository.custom.ShortURLRepositoryCustom;

public class ShortURLRepositoryImplementation implements ShortURLRepositoryCustom {
  @PersistenceContext private EntityManager em;

  private static final Logger log = LoggerFactory.getLogger(ShortURLRepositoryImplementation.class);

  @Override
  public ShortURL findByHash(String hash) {
    TypedQuery<ShortURL> query =
        em.createQuery("select a from ShortURL a where a.hash = ?1", ShortURL.class);
    query.setParameter(1, hash);
    return query.getSingleResult();
  }

  @Override
  public List<ShortURL> findByTarget(String target) {

    TypedQuery query =
        em.createQuery("select a from ShortURL a where a.target = ?1", ShortURL.class);
    query.setParameter(1, target);
    return query.getResultList();
  }
}
