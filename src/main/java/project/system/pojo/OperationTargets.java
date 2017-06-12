package project.system.pojo;

/**
 * Created by Errol on 16/10/13.
 */
public enum OperationTargets {

    Admin("系统管理员"),
    Agency("机构"),
    BookClassification("图书分类"),
    Client("微信用户"),
    Book("图书"),
    Comment("书评"),
    Reservation("借书记录")
    ;

    private String name;

    OperationTargets(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
