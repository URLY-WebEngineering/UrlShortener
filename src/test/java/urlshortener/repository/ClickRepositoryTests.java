package urlshortener.repository;

import static org.junit.Assert.*;

import java.math.BigInteger;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.domain.Click;
import urlshortener.fixtures.ClickFixture;
import urlshortener.fixtures.ShortURLFixture;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClickRepositoryTests {

  @Autowired private EntityManager entityManager;
  @Autowired private ShortURLRepository shortURLRepository;
  @Autowired private ClickRepository clickRepository;

  @Before
  public void setup() {
    shortURLRepository.save(ShortURLFixture.url1());
    shortURLRepository.save(ShortURLFixture.url2());
    shortURLRepository.flush();
  }

  @Test
  public void thatSavePersistsTheClickURL() {
    Click click = clickRepository.save(ClickFixture.click(ShortURLFixture.url1()));
    assertSame(
        1,
        ((BigInteger)
                entityManager.createNativeQuery("select count(*) from CLICK").getSingleResult())
            .intValue());
    assertNotNull(click);
    assertNotNull(click.getId());
  }

  @Test
  public void thatErrorsInSaveThrows() {
    assertThrows(
        InvalidDataAccessApiUsageException.class,
        () -> clickRepository.save(ClickFixture.click(ShortURLFixture.badUrl())));
  }

  @Test
  public void thatFindByKeyReturnsAURL() {
    clickRepository.save(ClickFixture.click(ShortURLFixture.url1()));
    clickRepository.save(ClickFixture.click(ShortURLFixture.url2()));
    clickRepository.save(ClickFixture.click(ShortURLFixture.url1()));
    clickRepository.save(ClickFixture.click(ShortURLFixture.url2()));
    clickRepository.save(ClickFixture.click(ShortURLFixture.url1()));
    assertEquals(3, clickRepository.findByHash(ShortURLFixture.url1()).size());
    assertEquals(2, clickRepository.findByHash(ShortURLFixture.url2()).size());
  }

  @Test
  public void thatFindByKeyReturnsEmpty() {
    clickRepository.save(ClickFixture.click(ShortURLFixture.url1()));
    clickRepository.save(ClickFixture.click(ShortURLFixture.url2()));
    clickRepository.save(ClickFixture.click(ShortURLFixture.url1()));
    clickRepository.save(ClickFixture.click(ShortURLFixture.url2()));
    clickRepository.save(ClickFixture.click(ShortURLFixture.url1()));
    assertEquals(0, clickRepository.findByHash(ShortURLFixture.url3()).size());
  }

  @Test
  public void thatDeleteDelete() {
    Long id1 = clickRepository.save(ClickFixture.click(ShortURLFixture.url1())).getId();
    Long id2 = clickRepository.save(ClickFixture.click(ShortURLFixture.url2())).getId();
    clickRepository.deleteById(id1);
    assertEquals(1, clickRepository.count());
    clickRepository.deleteById(id2);
    assertEquals(0, clickRepository.count());
  }
}
