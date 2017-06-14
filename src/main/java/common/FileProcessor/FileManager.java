package common.FileProcessor;

import common.Util.DateUtil;
import project.resource.pojo.UploadFolders;
import project.resource.properties.ServerProperties;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.Random;

/**
 * Created by Errol on 17/6/3.
 */
public class FileManager {

    private static String fileBathPath = ServerProperties.getInstance().getFileBasePath();

    public static void output(String filepath, HttpServletResponse response) {
        try {
            File file = new File(fileBathPath + filepath);
            System.out.println(file.getName());
            if (file.exists()) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("multipart/form-data");
                response.setHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"), "ISO_8859_1"));
                try {
                    FileInputStream fileInput = new FileInputStream(file);
                    BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);
                    byte[] bytes = new byte[bufferedInput.available()];
                    bufferedInput.read(bytes);
                    OutputStream output = response.getOutputStream();
                    output.write(bytes);
                    bufferedInput.close();
                    output.flush();
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(String fileName) {
        File file = new File(fileBathPath + fileName);
        file.delete();
    }

    public static String save(String filename, UploadFolders uploadFolder) {
        try {
            String folderName = uploadFolder + "/" + DateUtil.date2String(new Date(), DateUtil.PATTERN_H);
            File folder = new File(fileBathPath + folderName);
            if (!folder.exists() || !folder.isDirectory()) {
                folder.mkdir();
            }
            String newFilename = folderName + "/" + System.currentTimeMillis() + "_" + String.format("%02d", new Random().nextInt(100)) + filename.substring(filename.lastIndexOf("."));
            FileInputStream fileInputStream = new FileInputStream(fileBathPath + filename);
            FileOutputStream fileOutputStream = new FileOutputStream(fileBathPath + newFilename);
            byte[] buffer = new byte[1024];
            int hasRead;
            while ((hasRead = fileInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, hasRead);
            }
            fileInputStream.close();
            fileOutputStream.close();
            return newFilename;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

}
