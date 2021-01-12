package urlshortener.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import urlshortener.domain.ShortURL;
import urlshortener.repository.ShortURLRepository;

@RestController
public class UserController {

  private final ShortURLRepository shortURLRepository;

  public UserController(ShortURLRepository shortURLRepository) {
    this.shortURLRepository = shortURLRepository;
  }

  @Operation(summary = "Returns the name for the current logged in user.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "401",
            description = "User was not logged in and it does not have access to the information.",
            content = @Content)
      })
  @GetMapping(value = "/user")
  public ResponseEntity<?> getUser(Principal principal) {
    // Get username from principal (through Spring Security)
    Map<String, String> name = new HashMap<>();
    name.put("username", principal.getName());
    // Crete response
    HttpHeaders h = new HttpHeaders();
    return new ResponseEntity<>(name, h, HttpStatus.OK);
  }

  @Operation(summary = "Returns the collection of links shortened by the authenticated user.")
  @GetMapping(value = "/user/links")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The collection of shortened links was successfully retrieved.",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(
            responseCode = "401",
            description = "User was not logged in and it does not have access to the information.",
            content = @Content)
      })
  public ResponseEntity<?> customers(Principal principal) {
    // Get collection of url of user
    List<ShortURL> userURLs = shortURLRepository.findByOwner(principal.getName());
    Map<String, Object> response = new HashMap<>();
    response.put("urls", userURLs);
    // Create response
    HttpHeaders h = new HttpHeaders();
    return new ResponseEntity<>(response, h, HttpStatus.OK);
  }
}
