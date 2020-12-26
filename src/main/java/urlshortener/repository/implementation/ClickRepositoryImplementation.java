package urlshortener.repository.implementation;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import urlshortener.domain.Click;
import urlshortener.repository.custom.ClickRepositoryCustom;

public class ClickRepositoryImplementation implements ClickRepositoryCustom {

  @PersistenceContext private EntityManager em;

  @Override
  public List<Click> findByHash(String hash) {
    TypedQuery query = em.createQuery("select a from Click a where a.hash = ?1", Click.class);
    query.setParameter(1, hash);

    return query.getResultList();
  }
}
