package project.basic.model;

import project.basic.entity.Agency;
import project.operation.entity.Stacks;

/**
 * Created by Errol on 17/5/1.
 */
public class AgencyList {

    private String id;
    private String name;
    private String location;
    private String openTime;

    public AgencyList() {
    }

    public AgencyList(Object[] objects) {
        this.id = String.valueOf(objects[0]);
        this.name = String.valueOf(objects[1]);
        this.location = String.valueOf(objects[2]);
        this.openTime = String.valueOf(objects[3]);
    }
    public AgencyList(Agency agency, Stacks stacks) {
        this.id = String.valueOf(agency.getId());
        this.name = agency.getName();
        this.location = stacks.getLocation();
        this.openTime = stacks.getOpenTime();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getOpenTime() {
        return openTime;
    }
}
