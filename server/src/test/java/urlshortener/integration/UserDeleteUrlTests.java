package urlshortener.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext
public class UserDeleteUrlTests {

  @Value("${local.server.port}")
  private int port;

  private String websocketUri;

  private WebSocketStompClient stompClient;

  @Before
  public void setup() {
    websocketUri = String.format("ws://localhost:%d/ws", port);
    stompClient = createWebSocketClient();
  }

  @After
  public void tearDown() {
    stompClient.stop();
  }

  @Test
  public void clientConnected() throws InterruptedException, ExecutionException, TimeoutException {
    StompSession session =
        stompClient.connect(websocketUri, new StompSessionHandlerAdapter() {}).get(1, SECONDS);
    assertTrue(session.isConnected());
  }

  public WebSocketStompClient createWebSocketClient() {
    WebSocketStompClient stompClient =
        new WebSocketStompClient(
            new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
    stompClient.setMessageConverter(new StringMessageConverter());
    return stompClient;
  }
}
