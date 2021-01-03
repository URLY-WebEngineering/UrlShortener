package urlshortener.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;

@Service
public class QRService {
  public static byte[] getQRImage(String uri) throws IOException, WriterException {
    // Given an URI this function creates a qr code
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    QRCodeWriter writer = new QRCodeWriter();
    BitMatrix bitMatrix = writer.encode(uri, BarcodeFormat.QR_CODE, 300, 300);

    BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
    ImageIO.write(bufferedImage, "png", os);
    byte[] pngData = os.toByteArray();
    return pngData;
  }
}
