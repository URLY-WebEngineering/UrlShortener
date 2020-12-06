package urlshortener.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import urlshortener.domain.Click;
import urlshortener.repository.ClickRepository;

@Service
@EnableAsync
public class ClickService {
  private static final Logger log = LoggerFactory.getLogger(ClickService.class);

  private final ClickRepository clickRepository;

  // Template used to the send messages
  @Autowired private RabbitTemplate template;

  @Autowired private DirectExchange direct;

  public ClickService(ClickRepository clickRepository) {
    this.clickRepository = clickRepository;
  }

  public void saveClick(String hash, String ip) {
    Click cl = ClickBuilder.newInstance().hash(hash).createdNow().ip(ip).build();
    cl = clickRepository.save(cl);
    log.info(
        cl != null
            ? "[" + hash + "] saved with id [" + cl.getId() + "]"
            : "[" + hash + "] was not saved");
    sendClick();
  }

  @Async
  public void sendClick() {
    template.convertAndSend(direct.getName(), "responses_click", "add click");
  }
}
