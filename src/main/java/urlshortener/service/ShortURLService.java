package urlshortener.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Service;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ShortURLRepository;
import urlshortener.web.UrlShortenerController;

@Service
public class ShortURLService {

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
    return shortURLRepository.save(su);
  }

  public Long getTotalURL() {
    return shortURLRepository.count(); // NOSONAR
  }
}
