package project.basic.model;

import project.basic.entity.Agency;
import project.operation.entity.Stacks;

/**
 * Created by Errol on 17/5/1.
 */
public class AgencyList {

    private String id;
    private String name;
    private String code;
    private String location;
    private String openTime;

    public AgencyList() {
    }

    public AgencyList(Object[] objects) {
        this.id = String.valueOf(objects[0]);
        this.name = String.valueOf(objects[1]);
        this.code = String.valueOf(objects[2]);
        this.location = String.valueOf(objects[3]);
        this.openTime = String.valueOf(objects[4]);
    }
    public AgencyList(Agency agency, Stacks stacks) {
        this.id = String.valueOf(agency.getId());
        this.name = agency.getName();
        this.code = agency.getCode();
        this.location = stacks.getLocation();
        this.openTime = stacks.getOpenTime();
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

    public String getLocation() {
        return location;
    }

    public String getOpenTime() {
        return openTime;
    }
}
