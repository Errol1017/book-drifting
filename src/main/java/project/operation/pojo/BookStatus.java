package project.operation.pojo;

/**
 * Created by Errol on 17/4/16.
 */
public enum  BookStatus {

    //委托机构管理而尚未交给机构
    UNPREPARED("尚未入库"),

    //在机构或在所有人手里
    IN_STOCK("在架/闲置"),

    BORROWED("外借/流转"),

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
