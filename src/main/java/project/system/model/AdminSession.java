package project.system.model;

import project.system.entity.Admin;

import java.util.ArrayList;

/**
 * Created by Errol on 2016/9/28.
 */
public class AdminSession {

    private int id;
    private String name;
    private ArrayList<String> powerList;

    public AdminSession(Admin admin, ArrayList<String> powerList) {
        this.id = admin.getId();
        this.name = admin.getActualName().equals("")?admin.getAdminName():admin.getActualName();
        this.powerList = powerList;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public ArrayList<String> getPowerList() {
        return powerList;
    }

}
