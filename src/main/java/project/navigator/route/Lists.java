package project.navigator.route;

/**
 * Created by Errol on 17/4/20.
 */
public class Lists {

    public static final String AdminList = "/Pride and Prejudice";
    public static final String AdminLogList = "/Mansfield Park";

    public static String getList(String list) {
        switch (list) {
            case "AdminList": return Lists.AdminList;
            case "AdminLogList": return Lists.AdminLogList;
            default: return null;
        }
    }
}
