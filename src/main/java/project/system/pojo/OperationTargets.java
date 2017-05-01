package project.system.pojo;

/**
 * Created by Errol on 16/10/13.
 */
public enum OperationTargets {

    Admin("系统管理员"),
    Agency("固定起漂点"),
    BookClassification("图书分类"),
    ;

    private String name;

    OperationTargets(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
