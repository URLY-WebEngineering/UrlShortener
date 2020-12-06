package urlshortener.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class rabbitConfig {

  @Bean
  public DirectExchange direct() {
    return new DirectExchange("tut.direct");
  }
}
