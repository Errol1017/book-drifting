package common.FileProcessor.image;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import project.resource.properties.ServerProperties;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by Errol on 17/6/13.
 */
public class ImgUtil {

    private static String fileBathPath = ServerProperties.getInstance().getFileBasePath();

    public static String scale(String filename, int maxWidthOrHeight) {
        try {
            File file = new File(fileBathPath + filename);
            BufferedImage bufferedImage = ImageIO.read(file);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if (width > maxWidthOrHeight || height > maxWidthOrHeight) {
                BigDecimal ratio = BigDecimal.valueOf(width).divide(BigDecimal.valueOf(height), 10, BigDecimal.ROUND_HALF_UP);
                int newWidth;
                int newHeight;
                if (width >= height && width > maxWidthOrHeight) {
                    newWidth = maxWidthOrHeight;
                    newHeight = (int) (BigDecimal.valueOf(newWidth).divide(ratio, 10, BigDecimal.ROUND_HALF_UP).doubleValue());
                } else if (height > width && height > maxWidthOrHeight) {
                    newHeight = maxWidthOrHeight;
                    newWidth = (int) (BigDecimal.valueOf(newHeight).multiply(ratio).doubleValue());
                } else {
                    newWidth = width;
                    newHeight = height;
                }
                BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                newImage.getGraphics().drawImage(bufferedImage, 0, 0, newWidth, newHeight, null);
                String newFilename = "temp/" + System.currentTimeMillis() + "_" + String.format("%02d", new Random().nextInt(100)) + ".jpg";
                FileOutputStream fileOutputStream = new FileOutputStream(fileBathPath + newFilename);
                JPEGImageEncoder jpegImageEncoder = JPEGCodec.createJPEGEncoder(fileOutputStream);
                JPEGEncodeParam jpegEncodeParam = JPEGCodec.getDefaultJPEGEncodeParam(newImage);
                jpegEncodeParam.setQuality((float) 1.0, true);
                jpegImageEncoder.encode(newImage, jpegEncodeParam);
                fileOutputStream.close();
                return newFilename;
            } else {
                return filename;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    public static String cut(String filename, int ratioWidth, int ratioHeight) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(fileBathPath + filename));
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            BigDecimal scale = BigDecimal.valueOf(width).divide(BigDecimal.valueOf(height), 10, BigDecimal.ROUND_HALF_UP);
            BigDecimal ratio = BigDecimal.valueOf(ratioWidth).divide(BigDecimal.valueOf(ratioHeight), 10, BigDecimal.ROUND_HALF_UP);
            if (scale.compareTo(ratio) == 0) {
                return filename;
            } else {
                int startX, startY, endX, endY;
                if (scale.compareTo(ratio) == -1) {
                    int newHeight = (int) BigDecimal.valueOf(width).divide(ratio, 10, BigDecimal.ROUND_HALF_UP).doubleValue();
                    startX = 0;
                    endX = width;
                    startY = (height - newHeight) / 2;
                    endY = startY + newHeight;
                } else {
                    int newWidth = (int) BigDecimal.valueOf(height).multiply(ratio).doubleValue();
                    startY = 0;
                    endY = height;
                    startX = (width - newWidth) / 2;
                    endX = startX + newWidth;
                }
                BufferedImage newImage = new BufferedImage(endX - startX, endY - startY, BufferedImage.TYPE_INT_BGR);
                for (int x = startX; x < endX; ++x) {
                    for (int y = startY; y < endY; ++y) {
                        int rgb = bufferedImage.getRGB(x, y);
                        newImage.setRGB(x - startX, y - startY, rgb);
                    }
                }
                String newFilename = "temp/" + System.currentTimeMillis() + "_" + String.format("%02d", new Random().nextInt(100)) + ".jpg";
                ImageIO.write(newImage, "JPEG", new File(fileBathPath + newFilename));
                return newFilename;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
