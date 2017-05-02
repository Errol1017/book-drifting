package project.operation.pojo;

/**
 * Created by Errol on 17/4/16.
 */
public enum OwnerType {

    AGENCY("机构"),

    INDIVIDUAL("个人")
    ;

    private String name;

    OwnerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
