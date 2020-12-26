package urlshortener.service;

import static org.junit.Assert.*;
import static urlshortener.fixtures.ShortURLFixture.url1;
import static urlshortener.fixtures.ShortURLFixture.url2;
import static urlshortener.fixtures.ShortURLFixture.url3;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ShortURLRepository;
import urlshortener.service.exceptions.BadCustomBackhalfException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShortURLServiceTests {
  private ShortURLService shortURLService;
  @Autowired private ShortURLRepository repository;

  @Before
  public void setup() {
    shortURLService = new ShortURLService(repository);
  }

  @Test
  public void thatFindByKeyReturnsAURL() {
    ShortURL insertedURL1 =
        shortURLService.save(
            url1().getTarget(), url1().getSponsor(), url1().getIP(), url1().getQr() != null);
    ShortURL insertedURL2 =
        shortURLService.save(
            url3().getTarget(), url3().getSponsor(), url3().getIP(), url3().getQr() != null);
    ShortURL su = shortURLService.findByKey(insertedURL1.getHash());
    assertNotNull(su);
    assertEquals(url1().getTarget(), su.getTarget());
    assertEquals(insertedURL1.getHash(), su.getHash());
  }

  @Test
  public void thatFindByKeyReturnsAURLWithCustomBackhalf() throws BadCustomBackhalfException {
    String custombackhalf = "custombackhalf";
    ShortURL insertedURL1 =
        shortURLService.save(
            url1().getTarget(),
            url1().getSponsor(),
            custombackhalf,
            url1().getIP(),
            url1().getQr() != null);
    assertSame(custombackhalf, insertedURL1.getHash());
    ShortURL insertedURL2 =
        shortURLService.save(
            url3().getTarget(), url3().getSponsor(), url3().getIP(), url3().getQr() != null);
    ShortURL su = shortURLService.findByKey(custombackhalf);
    assertNotNull(su);
    assertEquals(url1().getTarget(), su.getTarget());
    assertEquals(custombackhalf, su.getHash());
  }

  @Test
  public void thatFindByKeyReturnsNullWhenFails() {
    repository.deleteAll();
    shortURLService.save(
        url1().getTarget(), url1().getSponsor(), url1().getIP(), url1().getQr() != null);
    ShortURL su = shortURLService.findByKey(url2().getHash());
    assertNull(su);
  }

  @Test
  public void thatFindByKeyReturnsNullWhenFailsWithCustomBackhalf()
      throws BadCustomBackhalfException {
    repository.deleteAll();
    String custombackhalf = "custombackhalf";
    shortURLService.save(
        url1().getTarget(),
        url1().getSponsor(),
        custombackhalf,
        url1().getIP(),
        url1().getQr() != null);
    ShortURL su = shortURLService.findByKey(url2().getHash());
    assertNull(su);
  }

  @Test
  public void thatBackhalfConformsToPattern() {
    String correctcustombackhalf1 = "custom";
    String correctcustombackhalf2 = "h_e_l-l-o-8_2";
    String correctcustombackhalf3 = "999999_";
    String badcustombackhalf1 = "_custom";
    String badcustombackhalf2 = "-custom";
    String badcustombackhalf3 = "custom?";
    String badcustombackhalf4 = "custom/";
    assertTrue(shortURLService.backhalfConformsToPattern(correctcustombackhalf1));
    assertTrue(shortURLService.backhalfConformsToPattern(correctcustombackhalf2));
    assertTrue(shortURLService.backhalfConformsToPattern(correctcustombackhalf3));
    assertFalse(shortURLService.backhalfConformsToPattern(badcustombackhalf1));
    assertFalse(shortURLService.backhalfConformsToPattern(badcustombackhalf2));
    assertFalse(shortURLService.backhalfConformsToPattern(badcustombackhalf3));
    assertFalse(shortURLService.backhalfConformsToPattern(badcustombackhalf4));
  }

  @Test
  public void thatSaveWithCustomBackhalfNotConformsToPatternThrows() {
    String custombackhalf = "_custom";
    Exception exception =
        assertThrows(
            BadCustomBackhalfException.class,
            () ->
                shortURLService.save(
                    url1().getTarget(),
                    url1().getSponsor(),
                    custombackhalf,
                    url1().getIP(),
                    url1().getQr() != null));
    assertEquals(
        "Backhalf should start with letters or numbers and be followed by letters, numbers, _ or -",
        exception.getMessage());
  }

  @Test
  public void thatBackhalfIsConflictive() {
    String correctcustombackhalf1 = "anything";
    String badcustombackhalf1 = "info";
    String badcustombackhalf2 = "link";
    String badcustombackhalf3 = "user";
    String badcustombackhalf4 = "qr";

    assertFalse(shortURLService.backhalfIsConflictive(correctcustombackhalf1));
    assertTrue(shortURLService.backhalfIsConflictive(badcustombackhalf1));
    assertTrue(shortURLService.backhalfIsConflictive(badcustombackhalf2));
    assertTrue(shortURLService.backhalfIsConflictive(badcustombackhalf3));
    assertTrue(shortURLService.backhalfIsConflictive(badcustombackhalf4));
  }

  @Test
  public void thatSaveWithCustomBackhalfIsConflictiveThrows() {
    String custombackhalf = "info";
    Exception exception =
        assertThrows(
            BadCustomBackhalfException.class,
            () ->
                shortURLService.save(
                    url1().getTarget(),
                    url1().getSponsor(),
                    custombackhalf,
                    url1().getIP(),
                    url1().getQr() != null));
    assertEquals("Backhalf conflicts with existing endpoints", exception.getMessage());
  }

  @Test
  public void thatSaveDuplicateWithCustomBackhalfThrows() throws BadCustomBackhalfException {
    String custombackhalf = "custom";
    shortURLService.save(
        url1().getTarget(),
        url1().getSponsor(),
        custombackhalf,
        url1().getIP(),
        url1().getQr() != null);
    Exception e =
        assertThrows(
            BadCustomBackhalfException.class,
            () ->
                shortURLService.save(
                    url3().getTarget(),
                    url3().getSponsor(),
                    custombackhalf,
                    url3().getIP(),
                    url3().getQr() != null));
    assertEquals("Backhalf already exists", e.getMessage());
  }
}
