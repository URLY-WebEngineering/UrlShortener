package systemInformation.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class rabbitConfig {
    // An exchange receive the messages from the producer and send it to the queues
    // A DirectExchange must know exactly what to do with a message it receives.
    // Its necessary to specific the key and the name of the queue
    @Bean
    public DirectExchange direct() {
        return new DirectExchange("tut.direct");
    }



}
