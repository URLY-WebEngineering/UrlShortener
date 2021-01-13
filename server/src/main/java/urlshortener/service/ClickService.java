package urlshortener.service;

import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import urlshortener.domain.Click;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ClickRepository;

@Service
@Transactional
public class ClickService {
  private static final Logger log = LoggerFactory.getLogger(ClickService.class);

  private final ClickRepository clickRepository;

  public ClickService(ClickRepository clickRepository) {
    this.clickRepository = clickRepository;
  }

  public void deleteClick(ShortURL su) {
    try {
      this.clickRepository.deleteAllByHash(su);
    } catch (Exception e) {
      log.info("[" + su.getHash() + "] was not deleted"); // NOSONAR
      throw e;
    }
  }

  public void saveClick(ShortURL shortURL, String ip) {
    try {
      Click cl = ClickBuilder.newInstance().shortURL(shortURL).createdNow().ip(ip).build();
      cl = clickRepository.save(cl);
      log.info("[" + shortURL.getHash() + "] saved with id [" + cl.getId() + "]"); // NOSONAR
    } catch (Exception e) {
      log.info("[" + shortURL.getHash() + "] was not saved"); // NOSONAR
      throw e;
    }
  }
}
