package urlshortener.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @Operation(summary = "Redirects the shortened URL identified by id to the original URL")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "307",
            description = "Shortened URL found and redirecting",
            content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "Shortened URL not found",
            content = @Content)
      })
  @GetMapping(value = "/{id:(?!link|index).*}")
  public ResponseEntity<?> redirectTo(
      @Parameter(description = "id of the shortened URL") @PathVariable String id,
      HttpServletRequest request) {
    ShortURL l = shortUrlService.findByKey(id);
    if (l != null) {
      clickService.saveClick(id, extractIP(request));
      return createSuccessfulRedirectToResponse(l);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Operation(summary = "Shortens a long URL into a shortened URL identified by an id")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Long URL shortened",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ShortURL.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Long URL could not be shortened",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", content = @Content)
      })
  @PostMapping(value = "/link")
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
