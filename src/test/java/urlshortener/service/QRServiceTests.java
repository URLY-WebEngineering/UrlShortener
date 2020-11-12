package urlshortener.service;
import static org.junit.Assert.assertTrue;
import com.google.zxing.WriterException;
import java.io.IOException;
import org.junit.Test;

public class QRServiceTests {

  @Test
  public void returnQRCorrectFormat() throws IOException, WriterException {
    // This test verify if the url has been convert to an qr code
    String test_url = "http://www.google.com/";
    String qrcode = QRService.getQRImage(test_url);
    assertTrue(qrcode.contains("data:image/png;base64,"));
  }
}
