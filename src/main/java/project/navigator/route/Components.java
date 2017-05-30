package project.navigator.route;

/**
 * Created by Errol on 17/4/20.
 */
public class Components {

    public static final String AdminForm_power = "/The Way of All Flesh";
    public static final String Invitation_Query_agency = "/Wuthering Heights";
    public static final String AdminLog_Query_target = "/David Copperfield";
    public static final String ClientForm_agencyId = "/Great Expectations";
    public static final String ClientList_appoint = "/Rebecca";

    public static String getComponent(String component) {
        switch (component) {
            case "AdminForm_power": return Components.AdminForm_power;
            case "Invitation_Query_agency": return Components.Invitation_Query_agency;
            case "AdminLog_Query_target": return Components.AdminLog_Query_target;
            case "ClientForm_agencyId": return Components.ClientForm_agencyId;
            case "ClientList_appoint": return Components.ClientList_appoint;
            default: return null;
        }
    }
}
