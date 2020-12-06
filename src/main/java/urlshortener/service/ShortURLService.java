package urlshortener.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ShortURLRepository;
import urlshortener.web.UrlShortenerController;

@Service
@EnableAsync
public class ShortURLService {
  // Template used to the send messages
  @Autowired private RabbitTemplate template;

  @Autowired private DirectExchange direct;

  @Async
  public void sendUrl() {
    template.convertAndSend(direct.getName(), "responses_url", "add url");
  }

  private final ShortURLRepository shortURLRepository;

  public ShortURLService(ShortURLRepository shortURLRepository) {
    this.shortURLRepository = shortURLRepository;
  }

  public ShortURL findByKey(String id) {
    return shortURLRepository.findByKey(id);
  }

  public ShortURL save(String url, String sponsor, String ip, boolean wantQr) {

    ShortURL su =
        ShortURLBuilder.newInstance()
            .target(url)
            .uri(
                (String hash) ->
                    linkTo(methodOn(UrlShortenerController.class).redirectTo(hash, null)).toUri())
            .sponsor(sponsor)
            .createdNow()
            .randomOwner()
            .temporaryRedirect()
            .treatAsSafe()
            .ip(ip)
            .unknownCountry()
            .qr(wantQr)
            .build();
    ShortURL found = shortURLRepository.findByKey(su.getHash());
    if (found == null) {
      sendUrl();
      return shortURLRepository.save(su);
    } else {
      return found;
    }
  }
}
