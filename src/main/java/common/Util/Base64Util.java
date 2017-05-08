package common.Util;

import common.CRUD.service.ComService;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Errol on 17/5/7.
 */
public class Base64Util {

    public static String img2String(String fileBasePath, String fileName) {
        try {
            InputStream inputStream;
            byte[] bytes;
            inputStream = new FileInputStream(fileBasePath + fileName);
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
