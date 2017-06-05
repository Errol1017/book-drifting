package project.navigator.route;

/**
 * Created by Errol on 17/4/20.
 */
public class Lists {

    public static final String AdminList = "/Pride and Prejudice";
    public static final String AdminLogList = "/Mansfield Park";
    public static final String InvitationList = "/Jane Eyre";
    public static final String AgencyList = "/The Way of All Flesh";
    public static final String ClassList = "/Alice's Adventures in Wonderland";
    public static final String ClientList = "/A Tale of Two Cities";
    public static final String BookList = "/Adventure of Sherlock Holmes";
    public static final String CommentList = "/The Winds of War";
    public static final String ReservationList = "/All the King's Men";

    public static String getList(String list) {
        switch (list) {
            case "AdminList": return Lists.AdminList;
            case "AdminLogList": return Lists.AdminLogList;
            case "InvitationList": return Lists.InvitationList;
            case "AgencyList": return Lists.AgencyList;
            case "ClassList": return Lists.ClassList;
            case "ClientList": return Lists.ClientList;
            case "BookList": return Lists.BookList;
            case "CommentList": return Lists.CommentList;
            case "ReservationList": return Lists.ReservationList;
            default: return null;
        }
    }
}
