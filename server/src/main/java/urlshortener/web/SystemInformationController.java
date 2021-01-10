package urlshortener.web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import urlshortener.domain.Information;

@Endpoint(id = "database")
@Configuration
@EnableAsync
@EnableScheduling
@Component
public class SystemInformationController {

  private AtomicInteger numClicks;
  private AtomicInteger numUsers;
  private AtomicInteger numURLs;
  private RabbitTemplate template;
  private DirectExchange direct;
  private AtomicBoolean readyresponse;

  @Bean
  public Queue responsesUsersQueue() {
    return new Queue("responses_users");
  }

  @Bean
  public Queue responsesURLQueue() {
    return new Queue("responses_url");
  }

  @Bean
  public Queue responsesClickQueue() {
    return new Queue("responses_click");
  }

  @Bean
  public Queue responsesQueue() {
    return new Queue("responses_queue");
  }
  // Bind the process to the queues
  // A binding is a relationship between an exchange and a queue
  @Bean
  public Binding bindingUsersResponses(DirectExchange direct, Queue responsesUsersQueue) {
    return BindingBuilder.bind(responsesUsersQueue).to(direct).with("responses_users");
  }

  @Bean
  public Binding bindingURLResponses(DirectExchange direct, Queue responsesURLQueue) {
    return BindingBuilder.bind(responsesURLQueue).to(direct).with("responses_url");
  }

  @Bean
  public Binding bindingClickResponses(DirectExchange direct, Queue responsesClickQueue) {
    return BindingBuilder.bind(responsesClickQueue).to(direct).with("responses_click");
  }

  @Bean
  public Binding bindingResponses(DirectExchange direct, Queue responsesQueue) {
    return BindingBuilder.bind(responsesQueue).to(direct).with("responses_queue");
  }

  public SystemInformationController(RabbitTemplate template, DirectExchange direct) {
    this.readyresponse = new AtomicBoolean(false);
    this.numClicks = new AtomicInteger(0);
    this.numURLs = new AtomicInteger(0);
    this.numUsers = new AtomicInteger(0);

    this.direct = direct;
    this.template = template;
  }

  // Petitions GET
  @ReadOperation
  public List<Information> getInformation() {
    List<Information> list = new ArrayList<>();
    list.add(
        new Information("URL.number", "Number of URL shortened stored in the database", numURLs));
    list.add(
        new Information(
            "Clicks.number", "Number of clicks to urls stored in  the database", numClicks));
    list.add(new Information("Users.number", "Number of users  on the database", numUsers));
    return list;
  }

  // Consumes the messages in the queue and updates the values
  @Async
  @RabbitListener(queues = "responses_users")
  public void receiveUsers(String message) {
    this.numUsers = parseMessage(message);
  }

  @Async
  @RabbitListener(queues = "responses_url")
  public void receiveUrl(String message) {
    this.numURLs = parseMessage(message);
  }

  @Async
  @RabbitListener(queues = "responses_click")
  public void receiveClick(String message) {
    this.numClicks = parseMessage(message);
  }

  // Updating system information ACK
  @Async
  @RabbitListener(queues = "responses_queue")
  public void receiveDone(String message) {
    this.readyresponse.set(true);
  }

  @Async("threadTaskScheduler")
  @Scheduled(fixedRate = 2000, initialDelay = 500)
  public void checkSystemInformation() {
    // First we check if the information if ready
    if (this.readyresponse.get()) {
      template.convertAndSend(direct.getName(), "request_queue", "get"); // NOSONAR
      this.readyresponse.set(false);
    } else {
      // If it is not ready , demand again the information
      requestUpdate(); // NOSONAR
    }
  }

  // First it ask for an update of the information
  @Async("threadTaskScheduler")
  @Scheduled(fixedRate = 1000, initialDelay = 500)
  public void updateInformation() {
    requestUpdate(); // NOSONAR
  }

  public void requestUpdate() {
    template.convertAndSend(direct.getName(), "request_queue", "update"); // NOSONAR
  }

  public AtomicInteger parseMessage(String message) {
    return (new AtomicInteger(Integer.parseInt(message)));
  }
}
