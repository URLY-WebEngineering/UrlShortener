package urlshortener.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.domain.Click;
import urlshortener.fixtures.ClickFixture;
import urlshortener.fixtures.ShortURLFixture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClickRepositoryTests {

  @Autowired private ClickRepository repository;

  @Autowired private ShortURLRepository shortUrlRepository;

  @Before
  public void setup() {
    shortUrlRepository.save(ShortURLFixture.url1());
    shortUrlRepository.save(ShortURLFixture.url2());
  }

  public void cleanRepository() {
    if (repository.count() > 0L) {
      repository.deleteAll();
    }
  }

  @Test
  public void thatSavePersistsTheClickURL() {
    cleanRepository();
    Click click = repository.save(ClickFixture.click(ShortURLFixture.url1()));
    assertSame(repository.count(), 1L);
    assertNotNull(click);
    assertNotNull(click.getId());
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void thatErrorsInSaveReturnsNull() {
    cleanRepository();
    assertNull(repository.save(ClickFixture.click(ShortURLFixture.badUrl())));
    assertEquals(repository.count(), 0L);
  }

  @Test
  public void thatFindByKeyReturnsAURL() {
    cleanRepository();
    repository.save(ClickFixture.click(ShortURLFixture.url1()));
    repository.save(ClickFixture.click(ShortURLFixture.url2()));
    repository.save(ClickFixture.click(ShortURLFixture.url1()));
    repository.save(ClickFixture.click(ShortURLFixture.url2()));
    repository.save(ClickFixture.click(ShortURLFixture.url1()));
    assertEquals(repository.findByHash(ShortURLFixture.url1().getHash()).size(), 3);
    assertEquals(repository.findByHash(ShortURLFixture.url2().getHash()).size(), 2);
  }

  @Test
  public void thatFindByKeyReturnsEmpty() {
    cleanRepository();
    repository.save(ClickFixture.click(ShortURLFixture.url1()));
    repository.save(ClickFixture.click(ShortURLFixture.url2()));
    repository.save(ClickFixture.click(ShortURLFixture.url1()));
    repository.save(ClickFixture.click(ShortURLFixture.url2()));
    repository.save(ClickFixture.click(ShortURLFixture.url1()));
    assertEquals(repository.findByHash(ShortURLFixture.badUrl().getHash()).size(), 0);
  }

  @Test
  public void thatDeleteDelete() {
    cleanRepository();
    Long id1 = repository.save(ClickFixture.click(ShortURLFixture.url1())).getId();
    Long id2 = repository.save(ClickFixture.click(ShortURLFixture.url2())).getId();
    repository.deleteById(id1);
    assertEquals(repository.count(), 1L);
    repository.deleteById(id2);
    assertEquals(repository.count(), 0L);
  }
}
