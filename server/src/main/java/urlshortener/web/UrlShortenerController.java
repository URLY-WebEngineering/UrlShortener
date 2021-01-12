package urlshortener.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import java.security.Principal;
import java.util.Optional;
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
import urlshortener.service.ShortURLService;
import urlshortener.service.URLStatusService;

@RestController
public class UrlShortenerController {
  private final ShortURLService shortUrlService;

  private final ClickService clickService;

  private final URLStatusService urlStatusService;

  public UrlShortenerController(
      ShortURLService shortUrlService,
      ClickService clickService,
      URLStatusService urlStatusService) {
    this.shortUrlService = shortUrlService;
    this.clickService = clickService;
    this.urlStatusService = urlStatusService;
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
            content = @Content),
        @ApiResponse(
            responseCode = "400",
            description = "Shortened URL not checked|reachable|safe",
            content = @Content)
      })
  @GetMapping(value = "/{id:(?!link|index).*}")
  public ResponseEntity<?> redirectTo(
      @Parameter(description = "id of the shortened URL") @PathVariable String id,
      HttpServletRequest request) {
    Optional<ShortURL> l = shortUrlService.findByKey(id);
    if (l.isPresent()) {
      clickService.saveClick(l.get(), extractIP(request));
      // Checking url status
      if (!l.get().getChecked()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UrlStatus.CHECKING.getStatus());
      }
      if (!l.get().getReachable()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, UrlStatus.UNREACHABLE.getStatus());
      }
      if (!l.get().getSafe()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UrlStatus.UNSAFE.getStatus());
      }
      // Everything is ok
      return createSuccessfulRedirectToResponse(l.get());
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
      @Parameter(description = "long url to shorten") @RequestParam("url") String url,
      @RequestParam(value = "sponsor", required = false) String sponsor,
      @RequestParam(value = "qrfeature", required = false) String qrfeature,
      @RequestParam(value = "custombackhalf", required = false) String custombackhalf,
      HttpServletRequest request,
      Principal principal)
      throws InterruptedException {

    try {
      UrlValidator urlValidator = new UrlValidator(new String[] {"http", "https"});
      if (!urlValidator.isValid(url)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UrlStatus.INVALID.getStatus());
      }

      ShortURL su;
      // TODO: check for @NotNull @NotBlank
      boolean wantQr = (qrfeature != null) && (qrfeature.equals("on"));
      String owner = principal != null ? principal.getName() : null;
      if ((custombackhalf != null) && (!custombackhalf.isEmpty())) {
        // ShortUrl is saved without URLStatus checked, it's pending
        su = shortUrlService.save(url, owner, custombackhalf, request.getRemoteAddr(), wantQr);
      } else {
        // ShortUrl is saved without URLStatus checked, it's pending
        su = shortUrlService.save(url, owner, request.getRemoteAddr(), wantQr);
      }

      // Check URLStatus
      urlStatusService.checkStatus(su); // Async
      // It's returned with a pending check
      HttpHeaders h = new HttpHeaders();
      h.setLocation(su.getUri());
      return new ResponseEntity<>(su, h, HttpStatus.CREATED);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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
