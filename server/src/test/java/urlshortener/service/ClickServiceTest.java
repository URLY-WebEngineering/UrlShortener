package urlshortener.service;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static urlshortener.fixtures.ClickFixture.*;
import static urlshortener.fixtures.ShortURLFixture.*;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.test.annotation.DirtiesContext;
import urlshortener.domain.Click;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ClickRepository;
import urlshortener.repository.ShortURLRepository;

@RunWith(MockitoJUnitRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClickServiceTest {

  @Mock ShortURLRepository shortURLRepository;

  @InjectMocks private ShortURLService shortURLService;

  @Mock ClickRepository ClickRepository;
  @Mock Logger log;
  @InjectMocks private ClickService clickService;

  @Test
  public void deleteOnCascade() {
    when(shortURLRepository.save(any(ShortURL.class))).thenReturn(urlIp());
    when(shortURLRepository.findById(any(String.class))).thenReturn(Optional.of(urlIp()));
    when(ClickRepository.save(any(Click.class))).thenReturn(click(urlIp()));

    ShortURL insertedURL1 =
        shortURLService.save(
            urlIp().getTarget(), urlIp().getSponsor(), urlIp().getIp(), urlIp().getQr() != null);

    Optional<ShortURL> su = shortURLService.findByKey(insertedURL1.getHash());
    assertTrue(su.isPresent());
    insertedURL1 = su.get();

    clickService.saveClick(insertedURL1, insertedURL1.getIp());
    clickService.saveClick(insertedURL1, insertedURL1.getIp());
    clickService.saveClick(insertedURL1, insertedURL1.getIp());
    clickService.deleteClick(insertedURL1);

    assertSame(ClickRepository.count(), 0L);
  }
}
