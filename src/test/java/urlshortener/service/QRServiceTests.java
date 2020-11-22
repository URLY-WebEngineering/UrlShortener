package urlshortener.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static urlshortener.service.QRService.getQRImage;

import com.google.zxing.WriterException;
import java.io.IOException;
import org.junit.Test;

public class QRServiceTests {

  @Test
  public void returnQRCorrectFormat() throws IOException, WriterException {
    // This test verify if the url has been convert to an qr code
    String test_url = "http://www.google.com/";
    byte[] qr = getQRImage(test_url);
    assertNotNull(qr);
  }
}
