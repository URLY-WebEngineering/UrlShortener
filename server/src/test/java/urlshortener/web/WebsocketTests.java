package urlshortener.web;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
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

  static final String WEBSOCKET_TOPIC = "/confirmation";
  static final String MESSAGE = "DELETE THIS URL";
  private String websocketUri;

  BlockingQueue<String> blockingQueue;
  WebSocketStompClient stompClient;

  @Before
  public void setup() {
    websocketUri = String.format("ws://localhost:%d/delete", port);
    blockingQueue = new LinkedBlockingDeque<>();
    stompClient =
        new WebSocketStompClient(
            new SockJsClient(asList(new WebSocketTransport(new StandardWebSocketClient()))));
  }

  @Test
  public void receiveAMessageFromTheServer() throws Exception {

    StompSession session =
        stompClient.connect(websocketUri, new StompSessionHandlerAdapter() {}).get(1, SECONDS);
    session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());

    session.send(WEBSOCKET_TOPIC, MESSAGE.getBytes());

    Assert.assertEquals(MESSAGE, blockingQueue.poll(1, SECONDS));
  }

  class DefaultStompFrameHandler implements StompFrameHandler {
    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
      return byte[].class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {
      blockingQueue.offer(new String((byte[]) o));
    }
  }
}
