package urlshortener.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import urlshortener.domain.ShortURL;
import urlshortener.service.QRService;
import urlshortener.service.ShortURLService;

@RestController
public class QRController {

  private final ShortURLService shortUrlService;

  public QRController(ShortURLService shortUrlService) {
    this.shortUrlService = shortUrlService;
  }

  @Operation(summary = "Generates a QR code for the shortened URL")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "QR created",
            content = @Content(mediaType = "image/png")),
        @ApiResponse(
            responseCode = "404",
            description = "id of shortened URL not found",
            content = @Content(mediaType = "application/json"))
      })
  @GetMapping(value = "/qr/{id:(?!link|index).*}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<?> generateQR(
      @Parameter(description = "id of the shortened url") @PathVariable String id,
      HttpServletRequest request)
      throws Exception {
    try {
      HttpHeaders h = new HttpHeaders(); // NOSONAR
      // Find the URL by the id
      ShortURL shorturl = shortUrlService.findByKey(id); // NOSONAR
      // Get the URI
      String location = shorturl.getUri().toString();
      // Create the QR code
      byte[] qrImage = QRService.getQRImage(location);
      // Specify the header and the content of the response
      h.setLocation(URI.create(location));
      h.setContentType(MediaType.IMAGE_PNG);
      h.setContentLength(qrImage.length);
      return new ResponseEntity<>(qrImage, h, HttpStatus.OK);

    } catch (Exception e) {
      // It could not find the URI
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID of shortened URL not found");
    }
  }
}
