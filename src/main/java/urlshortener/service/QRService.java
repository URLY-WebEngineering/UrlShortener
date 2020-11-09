package urlshortener.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// Tutorials:https://www.pixeltrice.com/generate-the-qr-code-using-spring-boot-application/
//          https://www.callicoder.com/generate-qr-code-in-java-using-zxing/


public class QRService {
    public static String getQRImage(String uri ) throws IOException, WriterException {
        //resultImage = new String("data:image/png;base64," + Base64.encode(os.toByteArray()));

        //return resultImage;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(uri, BarcodeFormat.QR_CODE, 400, 400);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(bufferedImage, "png", os);
        byte[] pngData = os.toByteArray();
        String resultImage = new String("data:image/png;base64," + Base64.encode(pngData));
        //return pngData;
        return resultImage;

    }

}
