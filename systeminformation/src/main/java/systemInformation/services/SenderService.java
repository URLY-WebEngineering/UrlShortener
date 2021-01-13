package systemInformation.services;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Configuration
@EnableAsync
@Component
public class SenderService {

  private RabbitTemplate template;
  private DirectExchange direct;
  private AccessService accessData;
  private KeycloakAccessService keycloakAccessService;

  private AtomicInteger numClicks;
  private AtomicInteger numUsers;
  private AtomicInteger numURLs;

  public SenderService(
      RabbitTemplate template,
      DirectExchange direct,
      AccessService accessData,
      KeycloakAccessService keycloakAccessService) {
    this.direct = direct;
    this.template = template;
    this.accessData = accessData;
    this.keycloakAccessService = keycloakAccessService;

    this.numClicks = new AtomicInteger(Math.toIntExact(accessData.getTotalClick()));
    this.numUsers = new AtomicInteger(0);
    this.numURLs = new AtomicInteger(Math.toIntExact(accessData.getTotalURL()));
  }

  // Bind the process to the queues
  // A binding is a relationship between an exchange and a queue
  @Bean
  public Binding bindingRequest(DirectExchange direct, Queue responsesRequest) {
    return BindingBuilder.bind(responsesRequest).to(direct).with("request_queue");
  }

  @Bean
  public Queue responsesRequest() {
    return new Queue("request_queue");
  }

  /**
   * Consume in the RabbitMQ queue and once it reads a message from the queue updates the values of
   * the attributes: this.numClicks this.numUsers this.numURLs When the values are updated run the
   * function sendResponse()
   *
   * @queue request_queue
   * @param in content of the message that is in the Rabbitmq queue
   * @return
   */
  @Async
  @RabbitListener(queues = "request_queue")
  public void listenRequest(String in) {
    if (in.equals("update")) {
      this.numClicks = new AtomicInteger(Math.toIntExact(accessData.getTotalClick()));
      this.numUsers = new AtomicInteger(keycloakAccessService.countNumberOfUser());
      this.numURLs = new AtomicInteger(Math.toIntExact(accessData.getTotalURL()));
      sendResponse();
    } else {
      sendUrl();
      sendClick();
      sendUser();
    }
  }

  /**
   * Write on the RabbitMQ queue the message "done" to inform the main server that the value
   * requested have been updated
   *
   * @queue responses_queue
   * @return
   */
  @Async
  public void sendResponse() {
    template.convertAndSend(direct.getName(), "responses_queue", "done");
  }

  /**
   * Write in the RabbitMQ queue the value of the attribute numURLs
   *
   * @queue responses_url
   * @return
   */
  @Async
  public void sendUrl() {
    template.convertAndSend(direct.getName(), "responses_url", this.numURLs.toString());
  }

  /**
   * Write on the RabbitMQ queue "responses_click" the value of the attribute numClicks
   *
   * @queue responses_click
   * @return
   */
  @Async
  public void sendClick() {
    template.convertAndSend(direct.getName(), "responses_click", this.numClicks.toString());
  }

  /**
   * Consume in the RabbitMQ queue the value of the attribute numUsers
   *
   * @queue responses_users
   * @return
   */
  @Async
  public void sendUser() {
    template.convertAndSend(direct.getName(), "responses_users", this.numUsers.toString());
  }
}
