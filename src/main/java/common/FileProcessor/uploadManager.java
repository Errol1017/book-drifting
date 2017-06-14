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

    //reduce image by equal proportion


}
