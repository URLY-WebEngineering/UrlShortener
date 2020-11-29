package urlshortener.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ReachabilityServiceTest {
  private ReachabilityUrlService reachabilityUrlService;

  @Before
  public void setup() {
    reachabilityUrlService = new ReachabilityUrlService();
  }

  @Test
  public void thatReturnTrueIfUrlIsReachable() {
    String url = "http://www.google.com/";
    assertTrue(reachabilityUrlService.isReachable(url));
  }

  @Test
  public void thatReturnFalseIfUrlIsUnreachable() {
    String url = "http://asdfghjklzxcvbnm.com";
    assertFalse(reachabilityUrlService.isReachable(url));
  }
}
