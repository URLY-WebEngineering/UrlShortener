package urlshortener.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ShortURLRepository;
import urlshortener.service.exceptions.BadCustomBackhalfException;
import urlshortener.web.UrlShortenerController;

@Service
public class ShortURLService {

  List<String> existingEndpoints = Arrays.asList("info", "link", "user", "qr");

  private final ShortURLRepository shortURLRepository;

  public ShortURLService(ShortURLRepository shortURLRepository) {
    this.shortURLRepository = shortURLRepository;
  }

  public ShortURL findByKey(String id) {
    return shortURLRepository.findByKey(id);
  }

  public ShortURL save(String url, String sponsor, String custombackhalf, String ip, boolean wantQr)
      throws BadCustomBackhalfException {

    if (!backhalfConformsToPattern(custombackhalf)) {
      throw new BadCustomBackhalfException(
          "Backhalf should start with letters or numbers and be followed by letters, numbers, _ or -");
    } else if (backhalfIsConflictive(custombackhalf)) {
      throw new BadCustomBackhalfException("Backhalf conflicts with existing endpoints");
    }

    ShortURL su =
        ShortURLBuilder.newInstance()
            .target(url)
            .customBackhalf(custombackhalf)
            .uri(
                (String hash) ->
                    linkTo(methodOn(UrlShortenerController.class).redirectTo(hash, null)).toUri())
            .sponsor(sponsor)
            .createdNow()
            .randomOwner()
            .temporaryRedirect()
            .notSafe()
            .ip(ip)
            .unknownCountry()
            .qr(wantQr)
            .notReachable()
            .notChecked()
            .build();
    try {
      return shortURLRepository.save(su);
    } catch (DuplicateKeyException e) {
      throw new BadCustomBackhalfException("Backhalf already exists");
    } catch (Exception e) {
      throw new BadCustomBackhalfException("Backhalf could not be inserted");
    }
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
            .notSafe()
            .ip(ip)
            .unknownCountry()
            .qr(wantQr)
            .notReachable()
            .notChecked()
            .build();
    try {
      return shortURLRepository.save(su);
    } catch (Exception e) {
      return su;
    }
  }

  public Long getTotalURL() {
    return shortURLRepository.count(); // NOSONAR
  }

  public boolean backhalfConformsToPattern(String custombackhalf) {
    // Check that it is a chain of letters, numbers, -, _
    Pattern pattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_-]*$");
    Matcher matcher = pattern.matcher(custombackhalf);
    return matcher.find();
  }

  public boolean backhalfIsConflictive(String custombackhalf) {
    // Check that it is not conflictive with already existing endpoints
    return existingEndpoints.contains(custombackhalf);
  }
}
