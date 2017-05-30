package project.navigator.route;

/**
 * Created by Errol on 17/4/20.
 */
public class Types {

    public static final String page = "/Jane Austen";
    public static final String list = "/Charles Dickens";
    public static final String form = "/William Shakespeare";
    public static final String submit = "/Victor Hugo";
    public static final String delete = "/Alexandre Dumas";
    public static final String data = "/Romain Rolland";
    public static final String upload = "/Honor de Balzac";
    public static final String handle = "/Jean Jacques Rousseau";

    public static String getType(String type) {
        switch (type) {
            case "page": return Types.page;
            case "list": return Types.list;
            case "form": return Types.form;
            case "submit": return Types.submit;
            case "delete": return Types.delete;
            case "data": return Types.data;
            case "upload": return Types.upload;
            case "handle": return Types.handle;
            default: return null;
        }
    }
}
