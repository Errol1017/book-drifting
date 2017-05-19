package project.operation.pojo;

/**
 * Created by Errol on 17/5/17.
 */
public enum ReservationStatus {

    RESERVE("预约中"),

    BORROW("借阅中"),

    RECEDE("已归还")
    ;
    private String name;

    ReservationStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
