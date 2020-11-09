package urlshortener.web;

import com.google.zxing.WriterException;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpServerErrorException;
import urlshortener.domain.ShortURL;
import urlshortener.service.QRService;
import urlshortener.service.SafeBrowsingService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;

@Controller
public class QRController {

    private final SafeBrowsingService safeBrowsingService;
    public QRController(SafeBrowsingService safeBrowsingService){

        this.safeBrowsingService = safeBrowsingService;
    }

//Provisional, currently this method is just for testing ,due to the way we return the qrcode is about to change
    @RequestMapping(value = "/qr", method = RequestMethod.POST, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<String> generateQR(@RequestParam("url") String url,
                                              HttpServletRequest request) throws IOException, WriterException {


        UrlValidator urlValidator = new UrlValidator(new String[] {"http", "https"});
        Boolean islocal= isLocal(url);
        if (urlValidator.isValid(url) || islocal ) {
            try { // Safe browsing checking
                if (safeBrowsingService.isSafe(url) || islocal) {
                    String qrImage = QRService.getQRImage(url);
                    HttpHeaders h = new HttpHeaders();
                    h.setLocation(URI.create(url));
                    h.setContentType(MediaType.IMAGE_PNG);
                    h.setContentLength(qrImage.length());
                    return new ResponseEntity<>(qrImage, h, HttpStatus.CREATED);
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


    private boolean isLocal(String url){
        String  local_host= "//localhost:8080/";
        return (url.contains(local_host));

    }

}
