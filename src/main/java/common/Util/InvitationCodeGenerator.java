package common.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Errol on 17/4/15.
 */
public class InvitationCodeGenerator {

    private static int number = 0;
    private static String month = DateUtil.date2String(new Date(), DateUtil.PATTERN_J);

    private static void checkNumber() {
        if (!month.equals(DateUtil.date2String(new Date(), DateUtil.PATTERN_J))) {
            number = 0;
            month = DateUtil.date2String(new Date(), DateUtil.PATTERN_J);
        }
    }

    private static String generate() {
        return month + (number++) + RandomUtil.getRandomString(6, RandomUtil.PATTEN_ALL_CHARS);
    }

    public static String getOne() {
        checkNumber();
        return generate();
    }

    public static List<String> getMany(int length) {
        checkNumber();
        List<String> res = new ArrayList<>();
        for (int i=0; i<length; i++){
            res.add(generate());
        }
        return res;
    }

}
