package urlshortener.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import urlshortener.repository.ShortURLRepository;

@RunWith(SpringRunner.class)
public class URLStatusServiceTests {

  @InjectMocks private URLStatusService urlStatusService;

  @Mock private ShortURLRepository shortURLRepository;

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
