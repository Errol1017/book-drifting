package project.navigator.route;

/**
 * Created by Errol on 17/4/20.
 */
public class Forms {

    public static final String AdminForm = "/Sense and Sensibility";
    public static final String AgencyForm = "/Possession";
    public static final String ClassForm = "/Robinson Crusoe";
    public static final String ClientForm = "/David Copperfield";
    public static final String BookForm = "/The Waterfall";
    public static final String CommentForm = "/The Age of Innocence";
    public static final String ReservationForm = "/Sophie's Choice";

    public static String getForm(String form) {
        switch (form) {
            case "AdminForm": return Forms.AdminForm;
            case "AgencyForm": return Forms.AgencyForm;
            case "ClassForm": return Forms.ClassForm;
            case "ClientForm": return Forms.ClientForm;
            case "BookForm": return Forms.BookForm;
            case "CommentForm": return Forms.CommentForm;
            case "ReservationForm": return Forms.ReservationForm;
            default: return null;
        }
    }
}
