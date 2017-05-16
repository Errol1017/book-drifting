package project.operation.pojo;

/**
 * Created by Errol on 17/4/16.
 */
public enum  BookStatus {

    UNPREPARED("未入库"),

    IN_STOCK("在架"),

    BORROWED("外借"),

    EXPIRED("到期未还")
    ;

    private String name;

    BookStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
