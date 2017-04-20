package project.system.util;

import common.Util.EncryptUtil;
import common.Util.RandomUtil;

import java.math.BigInteger;

/**
 * Created by Errol on 16/10/13.
 */
public class PasswordCoder {

    public static String getSalt(int length){
        return RandomUtil.getRandomString(length, RandomUtil.PATTEN_ALL_CHARS);
    }

    public static String codingPassword(String password, String salt) {
        byte[] pass = password.getBytes();
        byte[] SHAPass = EncryptUtil.encryptDigest(pass, EncryptUtil.KEY_SHA);
        StringBuffer saltReal = new StringBuffer();
        boolean getByte = false;
        for (int i=0; i<salt.length(); i++){
            if (Character.isDigit(salt.charAt(i))){
                getByte = !getByte;
            }
            if (getByte){
                saltReal.append(salt.charAt(i));
            }
        }
        int len = saltReal.length();
        if (len < 6) {
            salt = salt.substring(len, len + 6);
        }else if (len > 14){
            salt = salt.substring(len-6, len);
        }else {
            salt = saltReal.toString().substring(0, 6);
        }
        String saltPass = new BigInteger(SHAPass).toString(32) + salt;
        return new BigInteger(EncryptUtil.encryptDigest(saltPass.getBytes(), EncryptUtil.KEY_SHA)).toString(32);
    }

}
