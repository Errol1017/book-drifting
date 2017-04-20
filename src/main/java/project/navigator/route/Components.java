package project.navigator.route;

/**
 * Created by Errol on 17/4/20.
 */
public class Components {

    public static final String AdminForm_power = "/The Way of All Flesh";

    public static String getComponent(String component) {
        switch (component) {
            case "AdminForm_power": return Components.AdminForm_power;
            default: return null;
        }
    }
}
