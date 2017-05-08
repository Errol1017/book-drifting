package common.pojo;

/**
 * Created by Errol on 17/5/5.
 */
public enum Gender {

    MALE("男"),

    FEMALE("女"),

    UNKNOWN("未知");

    private String name;

    Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
