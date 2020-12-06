package urlshortener.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import urlshortener.repository.impl.ShortURLRepositoryImpl;

public class URLStatusServiceTests {
  private URLStatusService urlStatusService;

  @Before
  public void setup() {
    EmbeddedDatabase db =
        new EmbeddedDatabaseBuilder().setType(HSQL).addScript("schema-hsqldb.sql").build();
    JdbcTemplate jdbc = new JdbcTemplate(db);
    ShortURLRepositoryImpl repository = new ShortURLRepositoryImpl(jdbc);
    urlStatusService = new URLStatusService(repository);
  }

  @Test
  public void thatReturnTrueIfUrlIsReachable() {
    String url = "https://www.google.com/";
    assertTrue(urlStatusService.isReachable(url));
  }

  @Test
  public void thatReturnFalseIfUrlIsUnreachable() {
    String url = "http://asdfghjklzxcvbnm.com";
    assertFalse(urlStatusService.isReachable(url));
  }

  @Test
  public void thatReturnTrueIfUrlIsSafe() {
    String safeUrl = "http://www.google.com/";
    assertTrue(urlStatusService.isSafe(safeUrl));
  }

  @Test
  public void thatReturnFalseIfUrlIsNotSafe() {
    String unSafeUrl = "http://malware.testing.google.test/testing/malware/*";
    assertFalse(urlStatusService.isSafe(unSafeUrl));
  }
}
