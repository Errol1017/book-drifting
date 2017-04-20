package project.navigator.route;

/**
 * Created by Errol on 17/4/20.
 */
public class Forms {

    public static final String AdminForm = "/Sense and Sensibility";

    public static String getForm(String form) {
        switch (form) {
            case "AdminForm": return Forms.AdminForm;
            default: return null;
        }
    }
}
