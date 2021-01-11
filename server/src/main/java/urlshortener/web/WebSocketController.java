package urlshortener.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import urlshortener.domain.WebSocketMessage;
import urlshortener.service.ShortURLService;

@Controller
public class WebSocketController {

  private final ShortURLService shortUrlService;

  public WebSocketController(ShortURLService shortUrlService) {
    this.shortUrlService = shortUrlService;
  }

  @MessageMapping("/delete")
  @SendTo("/confirmation/message")
  public WebSocketMessage deleteUrl(WebSocketMessage message) throws Exception {
    shortUrlService.deleteByHash(message.getHash());
    return new WebSocketMessage(message.getUser(), message.getHash() + " has been deleted");
  }
}
