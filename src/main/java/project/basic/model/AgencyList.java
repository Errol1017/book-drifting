package project.basic.model;

import project.basic.entity.Agency;

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

    public AgencyList(Agency agency) {
        this.id = String.valueOf(agency.getId());
        this.name = agency.getName();
        this.location = agency.getLocation();
        this.openTime = agency.getOpenTime();
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
