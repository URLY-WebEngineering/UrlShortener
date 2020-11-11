package urlshortener.web;

import com.google.zxing.WriterException;
import java.io.IOException;
import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import urlshortener.service.QRService;
import urlshortener.service.SafeBrowsingService;

@Controller
public class QRController {

  @RequestMapping(value = "/qr", method = RequestMethod.POST, produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<String> generateQR(
      @RequestParam("url") String url, HttpServletRequest request)
      throws IOException, WriterException {
    String location = url;
    if (location != null && !location.isEmpty()) {
      try {
        String qrImage = QRService.getQRImage(location);
        HttpHeaders h = new HttpHeaders();
        h.setLocation(URI.create(location));
        h.setContentType(MediaType.IMAGE_PNG);
        h.setContentLength(qrImage.length());
        return new ResponseEntity<>(qrImage, h, HttpStatus.CREATED);

      } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }

    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}
