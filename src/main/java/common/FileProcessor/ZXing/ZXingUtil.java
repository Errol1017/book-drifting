package common.FileProcessor.ZXing;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import project.resource.properties.ServerProperties;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * Created by Errol on 17/6/3.
 */
public class ZXingUtil {

    private static String fileBathPath = ServerProperties.getInstance().getFileBasePath();
    private static final String path = "qrcode/";

    public static List<String> write2File(List<String> texts, List<String> names) {
        List<String> res = new ArrayList<>();
        int i = 0;
        for (String test: texts) {
            res.add(write2File(test, names==null?"":names.get(i)));
            i++;
        }
        return res;
    }

    public static String write2File(String text, String filename) {
        try {
            if (filename.equals("")) {
                filename = System.currentTimeMillis() + "_" + new Random().nextInt(100);
            }
            filename += ".jpg";
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300, hints);
            File img = new File(fileBathPath + path, filename);
            MatrixToImageWriter.writeToFile(bitMatrix, "jpg", img);
            return path + filename;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readValue(String fileName) {
        try {
            MultiFormatReader formatReader = new MultiFormatReader();
            File file = new File(fileBathPath + fileName);
            BufferedImage image = ImageIO.read(file);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            Result result = formatReader.decode(binaryBitmap, hints);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
