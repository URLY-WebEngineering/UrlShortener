package urlshortener.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import urlshortener.domain.Click;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ClickRepository;

@Service
public class ClickService {
  private static final Logger log = LoggerFactory.getLogger(ClickService.class);

  private final ClickRepository clickRepository;
  private final RabbitTemplate template;
  private final DirectExchange direct;

  public ClickService(
      ClickRepository clickRepository, RabbitTemplate template, DirectExchange direct) {
    this.clickRepository = clickRepository;
    this.template = template;
    this.direct = direct;
  }

  public void saveClick(ShortURL shortURL, String ip) {
    try {
      Click cl = ClickBuilder.newInstance().shortURL(shortURL).createdNow().ip(ip).build();
      cl = clickRepository.save(cl);
      template.convertAndSend(direct.getName(), "request_queue", "click");
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
