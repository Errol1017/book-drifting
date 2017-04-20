package project.system.pojo;

/**
 * Created by Errol on 16/10/13.
 */
public enum OperationTypes {

    Create("新 增"),

    Update("更 新"),

    Delete("删 除"),

    ;

    private String name;

    OperationTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
