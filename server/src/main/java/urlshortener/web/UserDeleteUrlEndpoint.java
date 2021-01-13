package urlshortener.web;

import java.util.Optional;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import urlshortener.domain.ShortURL;
import urlshortener.domain.ShortURLRepresentative;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;

@Controller
public class UserDeleteUrlEndpoint {

  private final ShortURLService shortURLService;
  private final ClickService clickService;

  public UserDeleteUrlEndpoint(ShortURLService shortURLService, ClickService clickService) {
    this.shortURLService = shortURLService;
    this.clickService = clickService;
  }

  @MessageMapping("/delete")
  // TODO: @SendTo or @SendToUser ?
  @SendTo("/confirmation/message")
  public ShortURLRepresentative deleteUrl(String hash) {
    Optional<ShortURL> url = shortURLService.findByKey(hash);
    if (url.isPresent()) {
      // clickService.deleteClick(url.get());
      // shortURLService.deleteByHash(url.get().getHash());
      return new ShortURLRepresentative(hash, "DELETED");
    } else {
      return new ShortURLRepresentative(hash, "NOT FOUND");
    }
  }
}
