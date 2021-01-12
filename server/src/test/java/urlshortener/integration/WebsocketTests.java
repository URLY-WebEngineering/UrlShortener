package urlshortener.integration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WebsocketTests {

  @Value("${local.server.port}")
  private int port;

  static final String ENDPOINT_SERVER = "/confirmation";
  private String websocketUri;

  BlockingQueue<String> blockingQueue;
  private WebSocketStompClient stompClient;

  @Before
  public void setup() {
    websocketUri = String.format("ws://localhost:%d/delete", port);
    blockingQueue = new LinkedBlockingDeque<>();
    stompClient = createWebSocketClient();
  }

  @After
  public void tearDown() {
    stompClient.stop();
  }

  @Test
  public void receiveAMessageFromTheServer() throws Exception {

    String message = "TEST MESSAGE";
    // Creating the Stomp clients to do the test
    StompSession session =
        stompClient.connect(websocketUri, new StompSessionHandlerAdapter() {}).get(1, SECONDS);

    Thread.sleep(200);

    session.subscribe(
        ENDPOINT_SERVER,
        new ClientFrameHandler(
            (payload) -> {
              blockingQueue.offer(payload.toString());
            }));

    Thread.sleep(200);

    session.send(ENDPOINT_SERVER, message);

    Thread.sleep(200);

    Assert.assertEquals(message, blockingQueue.poll(1, SECONDS));
  }

  @Test
  public void clientConnected() throws InterruptedException, ExecutionException, TimeoutException {

    String message = "TEST MESSAGE";
    // Creating the Stomp clients to do the test
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

  private static class ClientFrameHandler implements StompFrameHandler {
    private final Consumer<String> frameHandler;

    public ClientFrameHandler(Consumer<String> frameHandler) {
      this.frameHandler = frameHandler;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
      return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
      assert payload != null;
      frameHandler.accept(payload.toString());
    }
  }
}
