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
    public static final String Books_Query_agency = "/Howards End";
    public static final String Books_Query_status = "/The Human Factor";
    public static final String BookForm_bookClass = "/Jude the Obscure";
    public static final String Reservation_Query_status = "/The Catcher in the Rye";
    public static final String Export_Add_Invitation_Code = "/Lolita";
    public static final String Export_out_Invitation_Code = "/Gone with the Wind";
    public static final String Export_Add_QR_Code = "/The Call of the Wild";
    public static final String Export_out_QR_Code = "/The Old Man and the Sea";

    public static String getComponent(String component) {
        switch (component) {
            case "AdminForm_power": return Components.AdminForm_power;
            case "Invitation_Query_agency": return Components.Invitation_Query_agency;
            case "AdminLog_Query_target": return Components.AdminLog_Query_target;
            case "ClientForm_agencyId": return Components.ClientForm_agencyId;
            case "ClientList_appoint": return Components.ClientList_appoint;
            case "Books_Query_agency": return Components.Books_Query_agency;
            case "Books_Query_status": return Components.Books_Query_status;
            case "BookForm_bookClass": return Components.BookForm_bookClass;
            case "Reservation_Query_status": return Components.Reservation_Query_status;
            case "Export_Add_Invitation_Code": return Components.Export_Add_Invitation_Code;
            case "Export_out_Invitation_Code": return Components.Export_out_Invitation_Code;
            case "Export_Add_QR_Code": return Components.Export_Add_QR_Code;
            case "Export_out_QR_Code": return Components.Export_out_QR_Code;
            default: return null;
        }
    }
}
