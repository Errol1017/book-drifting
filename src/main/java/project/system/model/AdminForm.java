package project.system.model;

import project.system.entity.Admin;
import project.system.entity.AdminRole;

/**
 * Created by Errol on 2016/10/9.
 */
public class AdminForm {

    private String id;
    private String name;
    private String password;
    private String role;
    private String power;
    private String actualName;
    private String contactNumber;
    private String remark;

    public AdminForm() {
    }

    public AdminForm(Admin admin, AdminRole adminRole) {
        this.id = String.valueOf(admin.getId());
        this.name = admin.getAdminName();
        this.password = "";
        this.role = adminRole.getName();
        this.power = adminRole.getPower();
        this.actualName = admin.getActualName();
        this.contactNumber = admin.getContactNumber();
        this.remark = admin.getRemark();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getPower() {
        return power;
    }

    public String getActualName() {
        return actualName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getRemark() {
        return remark;
    }

}
