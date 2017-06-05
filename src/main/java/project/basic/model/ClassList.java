package project.basic.model;

import project.basic.entity.BookClassification;

/**
 * Created by Errol on 17/5/1.
 */
public class ClassList {

    private String id;
    private String name;
    private String code;

    public ClassList() {
    }

    public ClassList(BookClassification bc) {
        this.id = String.valueOf(bc.getId());
        this.name = bc.getName();
        this.code = bc.getCode();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
