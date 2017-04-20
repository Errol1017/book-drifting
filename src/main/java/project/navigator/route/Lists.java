package project.navigator.route;

/**
 * Created by Errol on 17/4/20.
 */
public class Lists {

    public static final String AdminList = "/Pride and Prejudice";

    public static String getList(String list) {
        switch (list) {
            case "AdminList": return Lists.AdminList;
            default: return null;
        }
    }
}
