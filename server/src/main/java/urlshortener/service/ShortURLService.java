package urlshortener.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ShortURLRepository;
import urlshortener.service.exceptions.BadCustomBackhalfException;
import urlshortener.web.UrlShortenerController;

/** Logic business to check and store shortened urls. */
@Service
@Transactional
public class ShortURLService {

  // List of endpoints already existing in the application that can not be used as custom
  // backhalves because they would collide
  List<String> existingEndpoints = Arrays.asList("info", "link", "user", "qr");

  private final ShortURLRepository shortURLRepository;

  public ShortURLService(ShortURLRepository shortURLRepository) {
    this.shortURLRepository = shortURLRepository;
  }

  public Optional<ShortURL> findByKey(String id) {
    return shortURLRepository.findById(id);
  }

  public void deleteByHash(String hash) {
    shortURLRepository.deleteById(hash);
  }

  /**
   * Saves a url with necessary information and a custom backhalf.
   *
   * @param url url to shorten
   * @param owner id of the user. It can be null if the user shortening the url is not logged in.
   * @param custombackhalf string with the custom backhalf used when shortening the url
   * @param ip of the user
   * @param wantQr if the user wants a QR
   * @return the saved shortened url
   * @throws BadCustomBackhalfException if there is a problem creating the shortened url and saving
   *     it.
   */
  public ShortURL save(String url, String owner, String custombackhalf, String ip, boolean wantQr)
      throws BadCustomBackhalfException {

    if (!backhalfConformsToPattern(custombackhalf)) {
      // Need that the custom backhalf conforms to the pattern to split the namespace of
      // backhalves between custom (not starting with underscore) and default (starting with
      // underscore)
      throw new BadCustomBackhalfException(
          "Backhalf should start with letters or numbers and be followed by letters, numbers, _ or -");
    } else if (backhalfIsConflictive(custombackhalf)) {
      // Custom bakchalf collides with existing endpoints of the application
      throw new BadCustomBackhalfException("Backhalf conflicts with existing endpoints");
    }

    ShortURL su =
        ShortURLBuilder.newInstance()
            .target(url)
            .customBackhalf(custombackhalf)
            .uri(
                (String hash) ->
                    linkTo(methodOn(UrlShortenerController.class).redirectTo(hash, null)).toUri())
            .sponsor(null)
            .createdNow()
            .owner(owner)
            .temporaryRedirect()
            .notSafe()
            .ip(ip)
            .unknownCountry()
            .qr(wantQr)
            .notReachable()
            .notChecked()
            .build();

    if (shortURLRepository.findById(custombackhalf).isPresent()) {
      // If backhalf exists already, it can not be created again and fails
      throw new BadCustomBackhalfException("Backhalf already exists");
    } else {
      try {
        return shortURLRepository.save(su);
      } catch (Exception e) {
        throw new BadCustomBackhalfException("Backhalf could not be inserted");
      }
    }
  }

  /**
   * Saves a url with necessary information.
   *
   * @param url url to shorten
   * @param owner id of the user. It can be null if the user shortening the url is not logged in.
   * @param ip of the user
   * @param wantQr if the user wants a QR
   * @return the saved shortened url
   */
  public ShortURL save(String url, String owner, String ip, boolean wantQr) {

    ShortURL su =
        ShortURLBuilder.newInstance()
            .target(url)
            .uri(
                (String hash) ->
                    linkTo(methodOn(UrlShortenerController.class).redirectTo(hash, null)).toUri())
            .sponsor(null)
            .createdNow()
            .owner(owner)
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

  /**
   * Checks that the custom backhalf conforms to a pattern of letters, numbers and underscore with
   * the first letter being a letter or a number.
   *
   * @param custombackhalf
   * @return true if conforms to the pattern. False otherwise.
   */
  public boolean backhalfConformsToPattern(String custombackhalf) {
    // Check that it is a chain of letters, numbers, -, _
    Pattern pattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_-]*$");
    Matcher matcher = pattern.matcher(custombackhalf);
    return matcher.find();
  }

  /**
   * Checks that the custom backhalf does not collide with existing endpoints of the application.
   *
   * @param custombackhalf
   * @return true if it collides with existing endpoints. False otherwise.
   */
  public boolean backhalfIsConflictive(String custombackhalf) {
    // Check that it is not conflictive with already existing endpoints
    return existingEndpoints.contains(custombackhalf);
  }
}
