package common.Util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Formatter;

/**
 * Created by Errol on 16/10/13.
 */
public class EncryptUtil {

    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";
    public static final String KEY_SHA_1 = "SHA-1";

    /**
     * MAC算法可选以下多种算法
     * <p>
     * <pre>
     * HmacMD5
     * HmacSHA1
     * HmacSHA256
     * HmacSHA384
     * HmacSHA512
     * </pre>
     */
    public static final String KEY_MAC = "HmacMD5";

    /**
     * BASE64
     */
    public static String encryptBASE64(byte[] key) {
        try {
            return (new BASE64Encoder()).encodeBuffer(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptBASE64(String key) {
        try {
            return (new BASE64Decoder()).decodeBuffer(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化HMAC密钥
     */
    public static String initMacKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
            SecretKey secretKey = keyGenerator.generateKey();
            return encryptBASE64(secretKey.getEncoded());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HMAC加密
     */
    public static byte[] encryptHMAC(byte[] data, String key) {
        try {
            SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            return mac.doFinal(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5 SHA
     */
    public static byte[] encryptDigest(byte[] data, String type) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(type);
            messageDigest.reset();
            messageDigest.update(data);
            return messageDigest.digest();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SHA-1
     */
    public static String encryptSHA1(String string) {
        try {
            byte[] digest = encryptDigest(string.getBytes("UTF-8"), EncryptUtil.KEY_SHA_1);
            Formatter formatter = new Formatter();
            for (byte b : digest)
            {
                formatter.format("%02x", b);
            }
            String result = formatter.toString();
            formatter.close();
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


}
