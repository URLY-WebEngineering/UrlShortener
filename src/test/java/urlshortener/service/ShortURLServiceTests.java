package urlshortener.service;

import static org.junit.Assert.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;
import static urlshortener.fixtures.ShortURLFixture.url1;
import static urlshortener.fixtures.ShortURLFixture.url2;
import static urlshortener.fixtures.ShortURLFixture.url3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ShortURLRepository;
import urlshortener.repository.impl.ShortURLRepositoryImpl;
import urlshortener.service.exceptions.BadCustomBackhalfException;

public class ShortURLServiceTests {
  private ShortURLService shortURLService;
  private EmbeddedDatabase db;
  private ShortURLRepository repository;
  private JdbcTemplate jdbc;

  @Before
  public void setup() {
    db = new EmbeddedDatabaseBuilder().setType(HSQL).addScript("schema-hsqldb.sql").build();
    jdbc = new JdbcTemplate(db);
    repository = new ShortURLRepositoryImpl(jdbc);
    shortURLService = new ShortURLService(repository);
  }

  @Test
  public void thatFindByKeyReturnsAURL() {
    ShortURL insertedURL1 =
        shortURLService.save(
            url1().getTarget(), url1().getSponsor(), url1().getIp(), url1().getQr() != null);
    ShortURL insertedURL2 =
        shortURLService.save(
            url3().getTarget(), url3().getSponsor(), url3().getIp(), url3().getQr() != null);
    ShortURL su = shortURLService.findByKey(insertedURL1.getHash());
    assertNotNull(su);
    assertSame(url1().getTarget(), su.getTarget());
    assertSame(insertedURL1.getHash(), su.getHash());
  }

  @Test
  public void thatFindByKeyReturnsAURLWithCustomBackhalf() throws BadCustomBackhalfException {
    String custombackhalf = "custombackhalf";
    ShortURL insertedURL1 =
        shortURLService.save(
            url1().getTarget(),
            url1().getSponsor(),
            custombackhalf,
            url1().getIp(),
            url1().getQr() != null);
    assertSame(custombackhalf, insertedURL1.getHash());
    ShortURL insertedURL2 =
        shortURLService.save(
            url3().getTarget(), url3().getSponsor(), url3().getIp(), url3().getQr() != null);
    ShortURL su = shortURLService.findByKey(custombackhalf);
    assertNotNull(su);
    assertSame(url1().getTarget(), su.getTarget());
    assertSame(custombackhalf, su.getHash());
  }

  @Test
  public void thatFindByKeyReturnsNullWhenFails() {
    shortURLService.save(
        url1().getTarget(), url1().getSponsor(), url1().getIp(), url1().getQr() != null);
    ShortURL su = shortURLService.findByKey(url2().getHash());
    assertNull(su);
  }

  @Test
  public void thatFindByKeyReturnsNullWhenFailsWithCustomBackhalf()
      throws BadCustomBackhalfException {
    String custombackhalf = "custombackhalf";
    shortURLService.save(
        url1().getTarget(),
        url1().getSponsor(),
        custombackhalf,
        url1().getIp(),
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
                    url1().getIp(),
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
                    url1().getIp(),
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
        url1().getIp(),
        url1().getQr() != null);
    Exception e =
        assertThrows(
            BadCustomBackhalfException.class,
            () ->
                shortURLService.save(
                    url3().getTarget(),
                    url3().getSponsor(),
                    custombackhalf,
                    url3().getIp(),
                    url3().getQr() != null));
    assertEquals("Backhalf already exists", e.getMessage());
  }

  @After
  public void shutdown() {
    db.shutdown();
  }
}
