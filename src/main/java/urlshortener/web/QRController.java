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
      HttpHeaders h = new HttpHeaders();
      ShortURL shorturl = shortUrlService.findByKey(id);
      String location = shorturl.getTarget();
      byte[] qrImage = QRService.getQRImage(location);
      h.setLocation(URI.create(location));
      h.setContentType(MediaType.IMAGE_PNG);
      h.setContentLength(qrImage.length);
      return new ResponseEntity<>(qrImage, h, HttpStatus.CREATED);
    } catch (Exception e) {

      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      // return new ResponseEntity<>("error", h,HttpStatus.CONFLICT);
    }
  }
}
