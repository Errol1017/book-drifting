package project.system.pojo;

/**
 * Created by Errol on 16/10/13.
 */
public enum OperationTargets {

    Admin("系统管理员"),

    ;

    private String name;

    OperationTargets(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
