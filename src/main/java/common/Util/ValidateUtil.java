package common.Util;

/**
 * Created by Errol on 17/4/20.
 */
public class ValidateUtil {

    public static boolean checkPositiveNumber(String s) {
        return s.matches("^[0-9]+");
    }

}
