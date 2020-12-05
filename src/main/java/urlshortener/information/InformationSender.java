package urlshortener.information;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import urlshortener.domain.Information;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@EnableScheduling
@EnableAsync
public class InformationSender {

  @Autowired private RabbitTemplate template;

  @Autowired private DirectExchange direct;

  private Integer numClicks;
  private Integer numUsers;
  private Integer numURLs;

  public InformationSender() {
    this.numClicks = 0;
    this.numUsers = 0;
    this.numURLs =0;
  }


  @Async
  @Scheduled(fixedDelay = 1000, initialDelay = 500)
  public void sendUrl() {
    template.convertAndSend(direct.getName(), "request_url", "url");
    template.convertAndSend(direct.getName(), "request_users", "users");
    template.convertAndSend(direct.getName(), "request_click", "click");
  }

  @Async
  @RabbitListener(queues = "responses_users")
  public void receiveUsers(String in) throws InterruptedException {
    this.numUsers= Integer.parseInt(in);
  }

  @Async
  @RabbitListener(queues = "responses_url")
  public void receiveUrl(String in) throws InterruptedException {
    this.numURLs= Integer.parseInt(in);
  }

  @Async
  @RabbitListener(queues = "responses_click")
  public void receiveClick(String in) throws InterruptedException {
    this.numClicks= Integer.parseInt(in);
  }
}
