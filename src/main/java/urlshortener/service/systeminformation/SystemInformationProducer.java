package urlshortener.service.systeminformation;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;

// The machine which is going to process the data
@EnableAsync
@Service
public class SystemInformationProducer {

  private final SystemCalculatorService calculator;

  @Autowired private RabbitTemplate template;

  @Autowired private DirectExchange direct;

  public SystemInformationProducer(ClickService clickService, ShortURLService shortUrlService) {
    this.calculator = new SystemCalculatorService(clickService, shortUrlService);
  }

  @Async
  @Scheduled(fixedRate = 1000, initialDelay = 250)
  public void sendUsers() {
    template.convertAndSend(direct.getName(), "responses_users", String.valueOf(0));
  }

  @Async
  @Scheduled(fixedRate = 1000, initialDelay = 500)
  public void sendUrl() {
    template.convertAndSend(
        direct.getName(), "responses_url", String.valueOf(calculator.getTotalURL()));
  }

  @Async
  @Scheduled(fixedRate = 1000, initialDelay = 750)
  public void sendClick() {
    template.convertAndSend(
        direct.getName(), "responses_click", String.valueOf(calculator.getTotalClick()));
  }
}
