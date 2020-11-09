package urlshortener.web;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import urlshortener.domain.ShortURL;
import urlshortener.service.ClickService;
import urlshortener.service.SafeBrowsingService;
import urlshortener.service.ShortURLService;

@RestController
public class UrlShortenerController {
  private final ShortURLService shortUrlService;

  private final ClickService clickService;

  private final SafeBrowsingService safeBrowsingService;

  public UrlShortenerController(
      ShortURLService shortUrlService,
      ClickService clickService,
      SafeBrowsingService safeBrowsingService) {
    this.shortUrlService = shortUrlService;
    this.clickService = clickService;
    this.safeBrowsingService = safeBrowsingService;
  }

  @RequestMapping(value = "/{id:(?!link|index).*}", method = RequestMethod.GET)
  public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
    ShortURL l = shortUrlService.findByKey(id);
    if (l != null) {
      clickService.saveClick(id, extractIP(request));
      return createSuccessfulRedirectToResponse(l);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }



  @RequestMapping(value = "/link", method = RequestMethod.POST)
  public ResponseEntity<ShortURL> shortener(
      @RequestParam("url") String url,
      @RequestParam(value = "sponsor", required = false) String sponsor,
      HttpServletRequest request) {
    UrlValidator urlValidator = new UrlValidator(new String[] {"http", "https"});
    if (urlValidator.isValid(url)) {
      try { // Safe browsing checking
        if (safeBrowsingService.isSafe(url)) {
          ShortURL su = shortUrlService.save(url, sponsor, request.getRemoteAddr());
          HttpHeaders h = new HttpHeaders();
          h.setLocation(su.getUri());
          return new ResponseEntity<>(su, h, HttpStatus.CREATED);
        } else {
          return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE); // Unsafe URL
        }
      } catch (HttpServerErrorException e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Error safety checking
      }

    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  private String extractIP(HttpServletRequest request) {
    return request.getRemoteAddr();
  }

  private ResponseEntity<?> createSuccessfulRedirectToResponse(ShortURL l) {
    HttpHeaders h = new HttpHeaders();
    h.setLocation(URI.create(l.getTarget()));
    return new ResponseEntity<>(h, HttpStatus.valueOf(l.getMode()));
  }
}
