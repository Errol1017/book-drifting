package project.system.model;

import project.system.entity.Admin;
import project.system.entity.AdminRole;

/**
 * Created by Errol on 2016/10/3.
 */
public class AdminList {

    private String id;
    private String name;
    private String role;
    private String actualName;

    public AdminList(Admin admin, AdminRole adminRole) {
        this.id = String.valueOf(admin.getId());
        this.name = admin.getAdminName();
        this.role = adminRole.getName();
        this.actualName = admin.getActualName();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getActualName() {
        return actualName;
    }
}
