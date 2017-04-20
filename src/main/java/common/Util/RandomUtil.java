package common.Util;

import java.util.Random;

/**
 * Created by Errol on 16/10/13.
 */
public class RandomUtil {

    public static final String PATTEN_ALL_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//    public static final String PATTEN_ALL_CHARS_NOT_CONFUSED = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String PATTEN_LETTERS = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String PATTEN_UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String PATTEN_LOWERCASE_LETTERS = "abcdefghijkllmnopqrstuvwxyz";
    public static final String PATTEN_NUMBERS = "0123456789";
    public static final String PATTEN_COLOR = "0123456789abcdef";

    public static String getRandomString(int length, String type) {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(type.charAt(random.nextInt(type.length())));
        }
        return stringBuffer.toString();
    }

}
