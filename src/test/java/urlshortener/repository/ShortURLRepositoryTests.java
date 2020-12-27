package urlshortener.repository;

import static org.junit.Assert.*;
import static urlshortener.fixtures.ShortURLFixture.*;

import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.domain.ShortURL;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShortURLRepositoryTests {

  @Autowired private ShortURLRepository repository;

  @Test
  public void thatSavePersistsTheShortURL() {
    repository.deleteAll();
    assertNotNull(repository.save(url1()));
    assertEquals(repository.count(), 1L);
  }

  @Test
  public void thatSaveSponsor() {
    repository.deleteAll();
    assertNotNull(repository.save(urlSponsor()));
    ShortURL newurl = repository.findByHash(urlSponsor().getHash());

    assertEquals(newurl.getSponsor(), urlSponsor().getSponsor());
  }

  @Test
  public void thatSaveSafe() {

    assertNotNull(repository.save(urlSafe()));
    assertEquals(repository.findByHash(urlSafe().getHash()).getSafe(), true);

    ShortURL notsafe = urlSafe();
    notsafe.setSafe(false);
    repository.save(notsafe);
    assertEquals(repository.findByHash(urlSafe().getHash()).getSafe(), false);
    notsafe.setSafe(true);
    repository.save(notsafe);
    assertEquals(repository.findByHash(urlSafe().getHash()).getSafe(), true);
  }

  @Test
  @Ignore
  public void thatSaveADuplicateHashThrowsException() {
    repository.deleteAll();
    repository.save(url1());
    // Assert is inserted
    assertEquals(1L, repository.count());
    // Assert exception for duplicate hash
    Exception exception = assertThrows(DuplicateKeyException.class, () -> repository.save(url1()));
    assertEquals(1L, repository.count());
  }

  @Test
  public void thatErrorsInSaveReturnsNull() {
    repository.deleteAll();
    Exception exception = assertThrows(Exception.class, () -> repository.save(badUrl()));
    assertEquals(repository.count(), 0L);
  }

  @Test
  public void thatFindByKeyReturnsAURL() {
    repository.save(url1());
    repository.save(url2());
    ShortURL su = repository.findByHash(url1().getHash());
    assertNotNull(su);
    assertEquals(su.getHash(), url1().getHash());
  }

  @Test
  public void thatFindByKeyReturnsNullWhenFails() {
    repository.deleteAll();
    repository.save(url1());
    assertNull(repository.findByHash(url2().getHash()));
  }

  @Test
  public void thatFindByTargetReturnsURLs() {
    repository.save(url1());
    repository.save(url2());
    repository.save(url3());
    List<ShortURL> sul = repository.findByTarget(url1().getTarget());
    assertEquals(sul.size(), 2);
    sul = repository.findByTarget(url3().getTarget());
    assertEquals(sul.size(), 1);
    sul = repository.findByTarget("dummy");
    assertEquals(sul.size(), 0);
  }

  @Test
  public void thatDeleteDelete() {
    repository.deleteAll();
    repository.save(url1());
    repository.save(url2());
    repository.deleteById(url1().getHash());
    assertEquals(repository.count(), 1L);
    repository.deleteById(url2().getHash());
    assertEquals(repository.count(), 0L);
  }

  @Test
  public void thatUpdateUpdate() {
    repository.save(url1());
    ShortURL su = repository.findByHash(url1().getHash());
    assertEquals(su.getTarget(), "http://www.unizar.es/");
    repository.save(url1modified());
    su = repository.findByHash(url1().getHash());
    assertEquals(su.getTarget(), "http://www.unizar.org/");
  }
}
