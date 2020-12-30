package urlshortener.repository;

import static org.junit.Assert.*;
import static urlshortener.fixtures.ShortURLFixture.badUrl;
import static urlshortener.fixtures.ShortURLFixture.url1;
import static urlshortener.fixtures.ShortURLFixture.url1modified;
import static urlshortener.fixtures.ShortURLFixture.url2;
import static urlshortener.fixtures.ShortURLFixture.url3;
import static urlshortener.fixtures.ShortURLFixture.urlSafe;
import static urlshortener.fixtures.ShortURLFixture.urlSponsor;

import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.domain.ShortURL;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ShortURLRepositoryTests {

  @Autowired private EntityManager entityManager;
  @Autowired private ShortURLRepository shortURLRepository;

  @Test
  public void thatSavePersistsTheShortURL() {
    assertNotNull(shortURLRepository.save(url1()));
    assertSame(
        1,
        ((BigInteger)
                entityManager.createNativeQuery("select count(*) from SHORTURL").getSingleResult())
            .intValue());
  }

  @Test
  public void thatSaveSponsor() {
    assertNotNull(shortURLRepository.save(urlSponsor()));
    assertSame(
        (String) entityManager.createNativeQuery("select sponsor from SHORTURL").getSingleResult(),
        urlSponsor().getSponsor());
  }

  @Test
  public void thatSaveSafe() {
    assertNotNull(shortURLRepository.save(urlSafe()));
    assertSame(
        true,
        (Boolean) entityManager.createNativeQuery("select safe from SHORTURL").getSingleResult());
    ShortURL shortURL = shortURLRepository.findByHash(urlSafe().getHash());
    shortURL.setSafe(false);
    shortURLRepository.save(shortURL);
    assertSame(
        false,
        (Boolean) entityManager.createNativeQuery("select safe from SHORTURL").getSingleResult());
    shortURL = shortURLRepository.findByHash(urlSafe().getHash());
    shortURL.setSafe(true);
    shortURLRepository.save(shortURL);
    assertSame(
        true,
        (Boolean) entityManager.createNativeQuery("select safe from SHORTURL").getSingleResult());
  }

  @Test
  public void thatErrorsInSaveReturnsNull() {
    Exception exception = assertThrows(Exception.class, () -> shortURLRepository.save(badUrl()));
    assertSame(
        0,
        ((BigInteger)
                entityManager.createNativeQuery("select count(*) from SHORTURL").getSingleResult())
            .intValue());
  }

  @Test
  public void thatFindByKeyReturnsAURL() {
    shortURLRepository.save(url1());
    shortURLRepository.save(url2());
    ShortURL su = shortURLRepository.findById(url1().getHash()).get();
    assertNotNull(su);
    assertSame(su.getHash(), url1().getHash());
  }

  @Test
  public void thatFindByKeyReturnsNullWhenFails() {
    shortURLRepository.save(url1());
    assertTrue(shortURLRepository.findById(url2().getHash()).isEmpty());
  }

  @Test
  public void thatFindByTargetReturnsURLs() {
    shortURLRepository.save(url1());
    shortURLRepository.save(url2());
    shortURLRepository.save(url3());
    List<ShortURL> sul = shortURLRepository.findByTarget(url1().getTarget());
    assertEquals(2, sul.size());
    sul = shortURLRepository.findByTarget(url3().getTarget());
    assertEquals(1, sul.size());
    sul = shortURLRepository.findByTarget("dummy");
    assertEquals(0, sul.size());
  }

  @Test
  public void thatDeleteDelete() {
    shortURLRepository.save(url1());
    shortURLRepository.save(url2());
    shortURLRepository.deleteById(url1().getHash());
    assertEquals(1, shortURLRepository.count());
    shortURLRepository.deleteById(url2().getHash());
    assertEquals(0, shortURLRepository.count());
  }

  @Test
  public void thatUpdateUpdate() {
    shortURLRepository.save(url1());
    ShortURL su = shortURLRepository.findByHash(url1().getHash());
    assertEquals(su.getTarget(), "http://www.unizar.es/");
    shortURLRepository.save(url1modified());
    su = shortURLRepository.findByHash(url1().getHash());
    assertEquals(su.getTarget(), "http://www.unizar.org/");
  }
}
