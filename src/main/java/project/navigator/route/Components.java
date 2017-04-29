package project.navigator.route;

/**
 * Created by Errol on 17/4/20.
 */
public class Components {

    public static final String AdminForm_power = "/The Way of All Flesh";
    public static final String Invitation_Query_status = "/Wuthering Heights";

    public static String getComponent(String component) {
        switch (component) {
            case "AdminForm_power": return Components.AdminForm_power;
            case "Invitation_Query_status": return Components.Invitation_Query_status;
            default: return null;
        }
    }
}
