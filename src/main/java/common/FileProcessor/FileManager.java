package common.FileProcessor;

import project.resource.properties.ServerProperties;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

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

    public static void deleteFile(String fileName) {
        File file = new File(fileBathPath + fileName);
        file.delete();
    }

}
