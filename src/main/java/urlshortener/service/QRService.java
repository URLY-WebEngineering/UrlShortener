package urlshortener.service;

import java.util.Base64;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// Tutorials:https://www.pixeltrice.com/generate-the-qr-code-using-spring-boot-application/
//          https://www.callicoder.com/generate-qr-code-in-java-using-zxing/

@Service
public class QRService {
    public static String getQRImage(String uri ) throws IOException, WriterException {

        Base64.Encoder enc = Base64.getEncoder();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(uri, BarcodeFormat.QR_CODE, 300, 300);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(bufferedImage, "png", os);
        byte[] pngData = os.toByteArray();
        return new String("data:image/png;base64," + enc.encodeToString(pngData));
    }

}
