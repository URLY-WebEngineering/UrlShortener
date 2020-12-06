package urlshortener.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;
import urlshortener.service.systeminformation.SystemInformationProducer;

@Configuration
public class rabbitConfig {

  @Bean
  public DirectExchange direct() {
    return new DirectExchange("tut.direct");
  }
  // The machine which is going to process the data
  private static class ReceiverConfig {
    @Bean
    public SystemInformationProducer receiver(
        ClickService clickService, ShortURLService shortUrlService) {
      return new SystemInformationProducer(clickService, shortUrlService);
    }
  }
}
