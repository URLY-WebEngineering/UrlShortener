package urlshortener.web;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import urlshortener.domain.ShortURL;
import urlshortener.domain.UrlStatus;
import urlshortener.service.ClickService;
import urlshortener.service.ReachabilityUrlService;
import urlshortener.service.SafeBrowsingService;
import urlshortener.service.ShortURLService;

@RestController
public class UrlShortenerController {
  private final ShortURLService shortUrlService;

  private final ClickService clickService;

  private final SafeBrowsingService safeBrowsingService;

  private final ReachabilityUrlService reachabilityUrlService;

  public UrlShortenerController(
      ShortURLService shortUrlService,
      ClickService clickService,
      SafeBrowsingService safeBrowsingService,
      ReachabilityUrlService reachabilityUrlService) {
    this.shortUrlService = shortUrlService;
    this.clickService = clickService;
    this.safeBrowsingService = safeBrowsingService;
    this.reachabilityUrlService = reachabilityUrlService;
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
    switch (checkStatus(url)) {
      case INVALID:
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UrlStatus.INVALID.getStatus());
      case UNREACHABLE:
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, UrlStatus.UNREACHABLE.getStatus());
      case UNSAFE:
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UrlStatus.UNSAFE.getStatus());
      case OK:
        // Create short url
        ShortURL su = shortUrlService.save(url, sponsor, request.getRemoteAddr());
        HttpHeaders h = new HttpHeaders();
        h.setLocation(su.getUri());
        return new ResponseEntity<>(su, h, HttpStatus.CREATED);
      default:
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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

  private UrlStatus checkStatus(String url) {
    UrlValidator urlValidator = new UrlValidator(new String[] {"http", "https"});
    // Checking switch
    if (!urlValidator.isValid(url)) return UrlStatus.INVALID;
    if (!reachabilityUrlService.isReachable(url)) return UrlStatus.UNREACHABLE;
    if (!safeBrowsingService.isSafe(url)) return UrlStatus.UNSAFE;
    // else
    return UrlStatus.OK;
  }
}
