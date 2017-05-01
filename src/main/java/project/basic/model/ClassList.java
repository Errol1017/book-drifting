package project.basic.model;

import project.basic.entity.BookClassification;

/**
 * Created by Errol on 17/5/1.
 */
public class ClassList {

    private String id;
    private String name;

    public ClassList() {
    }

    public ClassList(BookClassification bc) {
        this.id = String.valueOf(bc.getId());
        this.name = bc.getName();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
