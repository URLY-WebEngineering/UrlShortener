package urlshortener.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.repository.ShortURLRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class URLStatusServiceTests {
  private URLStatusService urlStatusService;

  @Autowired private ShortURLRepository repository;

  @Before
  public void setup() {
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
