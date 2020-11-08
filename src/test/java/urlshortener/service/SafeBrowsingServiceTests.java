package urlshortener.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SafeBrowsingServiceTests {

  private SafeBrowsingService safeBrowsingService;

  @Before
  public void setup() {
    safeBrowsingService = new SafeBrowsingService();
  }

  @Test
  public void thatReturnTrueIfUrlIsSafe() {
    String safeUrl = "http://www.google.com/";
    assertTrue(safeBrowsingService.isSafe(safeUrl));
  }

  @Test
  public void thatReturnFalseIfUrlIsNotSafe() {
    String unSafeUrl = "http://malware.testing.google.test/testing/malware/*";
    assertFalse(safeBrowsingService.isSafe(unSafeUrl));
  }
}
