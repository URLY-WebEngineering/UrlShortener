package urlshortener.information;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;

// The machine which is going to process the data
@EnableAsync
public class InformationReceiver {

  private final SystemCalculator calculator;

  @Autowired private RabbitTemplate template;

  @Autowired private DirectExchange direct;

  public InformationReceiver(ClickService clickService, ShortURLService shortUrlService) {
    this.calculator = new SystemCalculator(clickService, shortUrlService);
  }

  public void sendUsers() {
    String builder = new String(String.valueOf(0));
    template.convertAndSend(direct.getName(), "responses_users", builder);
  }

  public void sendUrl() {
    String builder = new String(String.valueOf(calculator.getTotalURL()));
    template.convertAndSend(direct.getName(), "responses_url", builder);
  }

  public void sendClick() {
    String builder = new String(String.valueOf(calculator.getTotalClick()));
    template.convertAndSend(direct.getName(), "responses_click", builder);
  }

  @Async
  @RabbitListener(queues = "request_users")
  public void receiveUsers(String in) throws InterruptedException {
    sendUsers();
  }

  @Async
  @RabbitListener(queues = "request_url")
  public void receiveURL(String in) throws InterruptedException {
    sendUrl();
  }

  @Async
  @RabbitListener(queues = "request_click")
  public void receiveClick(String in) throws InterruptedException {
    sendClick();
  }
}
