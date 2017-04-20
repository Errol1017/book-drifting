package common.FileProcessor;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import common.Util.DateUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Errol on 2016/7/6.
 */
public class uploadManager {

    //save upload files in temp folder
    public static List<String> uploadFiles(String fileBasePath, HttpServletRequest request){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        List<String> fileNames = new ArrayList<>();
        if (!multipartResolver.isMultipart(request)){
            fileNames.add("-5");
            fileNames.add("文件格式错误 或 大小超限");
        }else {
            try{
                String folderName = "temp/" + DateUtil.date2String(new Date(), DateUtil.PATTERN_I);
                File folder = new File(fileBasePath +"/" + folderName);
                if (!folder.exists() || !folder.isDirectory()) {
                    folder.mkdir();
                }
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
                List<MultipartFile> list = multipartHttpServletRequest.getFiles(multipartHttpServletRequest.getFileNames().next());
                for (MultipartFile multipartFile: list){
                        String originalName = multipartFile.getOriginalFilename();
                        String fileName = System.currentTimeMillis() + "_" + String.format("%02d", new Random().nextInt(100)) + originalName.substring(originalName.lastIndexOf("."));
                        File tempFile = new File(fileBasePath +"/" + folderName + "/" + fileName);
                        multipartFile.transferTo(tempFile);
                        fileNames.add(folderName + "/" + fileName);
                }
            }catch (Exception e){
                fileNames.clear();
                fileNames.add("-3");
                fileNames.add(e.getMessage());
            }
        }
        return fileNames;
    }

    //transfer files into real folder
//    public static String saveFiles(String fileBathPath, String fileName, Modules modules) {
//
//        String tempFilePath = fileBathPath + "/temp/" + fileName;
//        String realFilePath = fileBathPath + "/img/" + modules.toString().toLowerCase() + "/" + fileName;
//
//        try {
//            FileInputStream fileInputStream = new FileInputStream(tempFilePath);
//            FileOutputStream fileOutputStream = new FileOutputStream(realFilePath);
//            byte[] buffer = new byte[1024];
//            int hasRead;
//            while ((hasRead = fileInputStream.read(buffer)) > 0){
//                fileOutputStream.write(buffer, 0, hasRead);
//            }
//            fileInputStream.close();
//            fileOutputStream.close();
//        } catch (IOException ioe){
//            ioe.printStackTrace();
//        }
//
//        return modules.toString().toLowerCase() + "/" + fileName;
//    }

    //delete files
    public static void deleteFiles(String fileBathPath, String fileName) {

        String filePath = fileBathPath + "/img/" + fileName;
        File file = new File(filePath);
        file.delete();
    }

    //reduce image by equal proportion
    public static String reduceImgToGivenLength(String fileBathPath, String fileName, int maxWidth, int maxHeight) {
        String newFileName = System.currentTimeMillis() + "_" + (int)(Math.random()*100) + ".jpg";
        try{
            //读入文件
            File file = new File(fileBathPath + "/temp/" + fileName);
            // 构造Image对象
            BufferedImage bufferedImage = ImageIO.read(file);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if (width > maxWidth || height > maxHeight){
                BigDecimal ratio = BigDecimal.valueOf(width).divide(BigDecimal.valueOf(height), 10, BigDecimal.ROUND_HALF_UP);
                int newWidth;
                int newHeight;
                if (width >= height && width > maxWidth) {
                    newWidth = maxWidth;
                    newHeight = (int)(BigDecimal.valueOf(newWidth).divide(ratio, 10, BigDecimal.ROUND_HALF_UP).doubleValue());
                }else if (height > width && height > maxHeight) {
                    newHeight = maxHeight;
                    newWidth = (int)(BigDecimal.valueOf(newHeight).multiply(ratio).doubleValue());
                }else {
                    newWidth = width;
                    newHeight = height;
                }
                BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                newImage.getGraphics().drawImage(bufferedImage, 0, 0, newWidth, newHeight, null);
                FileOutputStream fileOutputStream = new FileOutputStream(fileBathPath + "/temp/" + newFileName);
                JPEGImageEncoder jpegImageEncoder = JPEGCodec.createJPEGEncoder(fileOutputStream);
                JPEGEncodeParam jpegEncodeParam = JPEGCodec.getDefaultJPEGEncodeParam(newImage);
                jpegEncodeParam.setQuality((float) 1.0, true);
                jpegImageEncoder.encode(newImage, jpegEncodeParam);
                fileOutputStream.close();
            }else {
                newFileName = fileName;
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return newFileName;
    }

}
