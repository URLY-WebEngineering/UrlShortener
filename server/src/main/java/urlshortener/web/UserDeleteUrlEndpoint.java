package urlshortener.web;

import java.util.Optional;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import urlshortener.domain.ShortURL;
import urlshortener.domain.ShortURLRepresentative;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;

/** Websockets endpoint for deleting shortened urls. */
@Controller
public class UserDeleteUrlEndpoint {

  private final ShortURLService shortURLService;
  private final ClickService clickService;

  public UserDeleteUrlEndpoint(ShortURLService shortURLService, ClickService clickService) {
    this.shortURLService = shortURLService;
    this.clickService = clickService;
  }

  /**
   * Endpoint that receives a message with the hash of the url to delete. If it exists, it deletes
   * it and sends a message informing about that to the user. If it does not exist, it sends a
   * message informing that the url to delete was not found.
   *
   * @param hash identifier of the shortened url
   * @return message with information about the url to delete
   */
  @MessageMapping("/delete")
  @SendToUser("/confirmation/message")
  public ShortURLRepresentative deleteUrl(String hash) {
    try {
      Optional<ShortURL> url = shortURLService.findByKey(hash);
      if (url.isPresent()) {
        deleteClicksAndShortURL(url.get());
        return new ShortURLRepresentative(hash, "DELETED");
      } else {
        return new ShortURLRepresentative(hash, "NOT FOUND");
      }
    } catch (Exception e) {
      return new ShortURLRepresentative(hash, "SERVER ERROR: COULD NOT BE DELETED");
    }
  }

  /**
   * Deletes a short url and the related clicks to that url.
   *
   * @param su shortened url to delete
   */
  @Async
  public void deleteClicksAndShortURL(ShortURL su) {
    clickService.deleteClick(su);
    shortURLService.deleteByHash(su.getHash());
  }
}
