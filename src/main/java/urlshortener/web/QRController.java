package urlshortener.web;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import urlshortener.domain.ShortURL;
import urlshortener.service.QRService;
import urlshortener.service.ShortURLService;

@Controller
public class QRController {

  private final ShortURLService shortUrlService;

  public QRController(ShortURLService shortUrlService) {
    this.shortUrlService = shortUrlService;
  }

  @RequestMapping(
      value = "/qr/{id:(?!link|index).*}",
      method = RequestMethod.GET,
      produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<?> generateQR(@PathVariable String id, HttpServletRequest request)
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
      return new ResponseEntity<>(qrImage, h, HttpStatus.CREATED);

    } catch (Exception e) {
      // It could not find the URI
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
