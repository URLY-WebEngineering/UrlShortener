package urlshortener.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import urlshortener.domain.Click;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ClickRepository;

@Service
public class ClickService {
  private static final Logger log = LoggerFactory.getLogger(ClickService.class);

  private final ClickRepository clickRepository;

  public ClickService(ClickRepository clickRepository) {
    this.clickRepository = clickRepository;
  }

  public void saveClick(ShortURL shortURL, String ip) {
    try {
      Click cl = ClickBuilder.newInstance().shortURL(shortURL).createdNow().ip(ip).build();
      cl = clickRepository.save(cl);
      log.info("[" + shortURL.getHash() + "] saved with id [" + cl.getId() + "]");
    } catch (Exception e) {
      log.info("[" + shortURL.getHash() + "] was not saved");
      throw e;
    }
  }

  public Long getTotalClick() {
    return clickRepository.count(); // NOSONAR
  }
}