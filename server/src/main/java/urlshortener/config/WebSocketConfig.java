package urlshortener.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    // Endpoint where the messages are going to be sent by the server
    config.enableSimpleBroker("/confirmation");
    // Endpoint which will receive the messages from the client
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // Add two endpoints where the clients will write its messages
    // we are also adding here an endpoint that works without the SockJS for the sake of elasticity.
    registry.addEndpoint("/delete").withSockJS();
  }
}
