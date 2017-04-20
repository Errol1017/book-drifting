package project.system.entity;

import project.navigator.model.Navigation;
import project.system.model.AdminForm;

import javax.persistence.*;

/**
 * Created by Errol on 2016/9/26.
 */
@Entity
@Table(name = "admin_role")
public class AdminRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "smallint unsigned")
    private int id;
    //所在部门
    @Column(nullable = false)
    private String department = "";
    //角色名
    @Column(nullable = false)
    private String name;
    //权限
    @Column(nullable = false)
    private String power;
    //备注
    @Column(nullable = false)
    private String remark = "";

    public AdminRole() {
    }

    public AdminRole(AdminForm form) {
        this.name = form.getRole();
        this.power = form.getPower();
    }

    public AdminRole modify(AdminForm form) {
        this.name = form.getRole();
        this.power = form.getPower();
        return this;
    }

    /**
     * 开发阶段初始化root管理员角色
     * @return
     */
    public AdminRole addRootAdminRole() {
//        this.id = 1;
        this.name = "root";
        this.power = Navigation.getInstance().getRootPowerString();
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
