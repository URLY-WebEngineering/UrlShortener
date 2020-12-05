package urlshortener.information;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;

@Configuration
public class rabbitConfig {

  @Bean
  public DirectExchange direct() {
    return new DirectExchange("tut.direct");
  }
  // The machine which is going to process the data
  private static class ReceiverConfig {

    @Bean
    public Queue UsersRequestQueue() {
      return new Queue("request_users");
    }

    @Bean
    public Binding bindingUsersRequest(DirectExchange direct, Queue  UsersRequestQueue) {
      return BindingBuilder.bind(UsersRequestQueue).to(direct).with("request_users");
    }

    @Bean
    public  Queue URLRequestQueue(){
      return new Queue("request_url");
    }

    @Bean
    public Binding bindingURLRequest(DirectExchange direct, Queue  URLRequestQueue) {
      return BindingBuilder.bind(URLRequestQueue).to(direct).with("request_url");
    }


    @Bean
    public  Queue ClickRequestQueue(){
      return new Queue("request_click");
    }

    @Bean
    public Binding bindingClickRequest(DirectExchange direct, Queue  ClickRequestQueue) {
      return BindingBuilder.bind(ClickRequestQueue).to(direct).with("request_click");
    }



    @Bean
    public InformationReceiver receiver(ClickService clickService, ShortURLService shortUrlService) {
      return new InformationReceiver(clickService, shortUrlService);
    }
  }



  private static class SenderConfig {

    @Bean
    public Queue ResponsesUsersQueue() {
      return new Queue("responses_users");
    }

    @Bean
    public Binding bindingUsersResponses(DirectExchange direct, Queue ResponsesUsersQueue) {
      return BindingBuilder.bind(ResponsesUsersQueue).to(direct).with("responses_users");
    }

    @Bean
    public Queue ResponsesURLQueue() {
      return new Queue("responses_url");
    }

    @Bean
    public Binding bindingURLResponses(DirectExchange direct, Queue ResponsesURLQueue) {
      return BindingBuilder.bind(ResponsesURLQueue).to(direct).with("responses_url");
    }


    @Bean
    public Queue ResponsesClickQueue() {
      return new Queue("responses_click");
    }

    @Bean
    public Binding bindingClickResponses(DirectExchange direct, Queue ResponsesClickQueue) {
      return BindingBuilder.bind(ResponsesClickQueue).to(direct).with("responses_click");
    }


    @Bean
    public InformationSender sender() {
      return new InformationSender();
    }
  }
}
