package urlshortener.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static urlshortener.fixtures.ShortURLFixture.*;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.annotation.DirtiesContext;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ShortURLRepository;
import urlshortener.service.exceptions.BadCustomBackhalfException;

@RunWith(MockitoJUnitRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ShortURLServiceTests {

  @Mock ShortURLRepository shortURLRepository;

  @InjectMocks private ShortURLService shortURLService;

  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void thatFindByKeyReturnsAURL() {
    when(shortURLRepository.save(any(ShortURL.class))).thenReturn(url1()).thenReturn(url3());
    when(shortURLRepository.findById(any(String.class))).thenReturn(Optional.of(url1()));

    ShortURL insertedURL1 =
        shortURLService.save(
            url1().getTarget(), url1().getSponsor(), url1().getIp(), url1().getQr() != null);
    ShortURL insertedURL2 =
        shortURLService.save(
            url3().getTarget(), url3().getSponsor(), url3().getIp(), url3().getQr() != null);
    Optional<ShortURL> su = shortURLService.findByKey(insertedURL1.getHash());
    assertTrue(su.isPresent());
    assertSame(url1().getTarget(), su.get().getTarget());
    assertSame(insertedURL1.getHash(), su.get().getHash());
  }

  @Test
  public void thatFindByKeyReturnsAURLWithCustomBackhalf() throws BadCustomBackhalfException {
    String custombackhalf = "custombackhalf";
    when(shortURLRepository.findById(any(String.class)))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(url1Custom(custombackhalf)));
    when(shortURLRepository.save(any(ShortURL.class)))
        .thenReturn(url1Custom(custombackhalf))
        .thenReturn(url3());

    ShortURL insertedURL1 =
        shortURLService.save(
            url1Custom(custombackhalf).getTarget(),
            url1Custom(custombackhalf).getSponsor(),
            url1Custom(custombackhalf).getHash(),
            url1Custom(custombackhalf).getIp(),
            url1Custom(custombackhalf).getQr() != null);
    assertSame(custombackhalf, insertedURL1.getHash());
    ShortURL insertedURL2 =
        shortURLService.save(
            url3().getTarget(), url3().getSponsor(), url3().getIp(), url3().getQr() != null);
    Optional<ShortURL> su = shortURLService.findByKey(custombackhalf);
    assertTrue(su.isPresent());
    assertSame(url1Custom(custombackhalf).getTarget(), su.get().getTarget());
    assertSame(custombackhalf, su.get().getHash());
  }

  @Test
  public void thatFindByKeyReturnsNullWhenFails() {
    when(shortURLRepository.findById(any(String.class))).thenReturn(Optional.empty());
    when(shortURLRepository.save(any(ShortURL.class))).thenReturn(url1());

    shortURLService.save(
        url1().getTarget(), url1().getSponsor(), url1().getIp(), url1().getQr() != null);
    Optional<ShortURL> su = shortURLService.findByKey(url2().getHash());
    assertTrue(su.isEmpty());
  }

  @Test
  public void thatFindByKeyReturnsNullWhenFailsWithCustomBackhalf()
      throws BadCustomBackhalfException {
    when(shortURLRepository.findById(any(String.class)))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.empty());
    when(shortURLRepository.save(any(ShortURL.class))).thenReturn(url1());

    String custombackhalf = "custombackhalf";
    shortURLService.save(
        url1().getTarget(),
        url1().getSponsor(),
        custombackhalf,
        url1().getIp(),
        url1().getQr() != null);
    Optional<ShortURL> su = shortURLService.findByKey(url2().getHash());
    assertTrue(su.isEmpty());
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
    when(shortURLRepository.findById(any(String.class)))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(url1()));
    when(shortURLRepository.save(any(ShortURL.class))).thenReturn(url1());

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
}
