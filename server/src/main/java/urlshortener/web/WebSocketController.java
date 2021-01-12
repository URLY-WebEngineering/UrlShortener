package urlshortener.web;

import java.util.Optional;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import urlshortener.domain.ShortURL;
import urlshortener.domain.WebSocketMessage;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;

@Controller
public class WebSocketController {

  private final ShortURLService shortUrlService;
  private final ClickService clickService;

  public WebSocketController(ShortURLService shortUrlService, ClickService clickService) {
    this.shortUrlService = shortUrlService;
    this.clickService = clickService;
  }

  @MessageMapping("/delete")
  @SendTo("/confirmation/message")
  public WebSocketMessage deleteUrl(WebSocketMessage message) throws Exception {
    Optional<ShortURL> url = shortUrlService.findByKey(message.getHash());
    if (url.isPresent()) {
      clickService.deleteClick(url.get());
      shortUrlService.deleteByHash(url.get().getHash());
      return new WebSocketMessage(message.getUser(), message.getHash() + "has been deleted");
    } else {
      return new WebSocketMessage(message.getUser(), message.getHash() + " was not found");
    }
  }
}
