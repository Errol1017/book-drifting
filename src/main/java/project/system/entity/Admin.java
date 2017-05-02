package project.system.entity;

import project.system.model.AdminForm;
import common.Util.SecretKeyCoder;

import javax.persistence.*;

/**
 * Created by Errol on 2016/9/26.
 */
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "smallint unsigned")
    private int id;
    //用户名
    @Column(unique = true, nullable = false)
    private String adminName;
    //密码
    @Column(nullable = false)
    private String password;
    //盐值
    @Column(nullable = false)
    private String salt;
    //角色id
    @Column(nullable = false, columnDefinition = "smallint unsigned")
    private int roleId;
    //真实姓名
    @Column(nullable = false)
    private String actualName = "";
    //联系方式
    @Column(nullable = false)
    private String contactNumber = "";
    //备注
    @Column(nullable = false)
    private String remark = "";

    public Admin() {
    }

    public Admin(AdminForm form, AdminRole role) {
        this.adminName = form.getName();
        this.salt = SecretKeyCoder.getSalt(20);
        this.password = SecretKeyCoder.codingPassword(form.getPassword(), this.salt);
        this.roleId = role.getId();
        this.actualName = form.getActualName();
        this.contactNumber = form.getContactNumber();
        this.remark = form.getRemark();
    }

    public Admin modify(AdminForm form) {
        this.adminName = form.getName();
        if (form.getPassword() != "") {
            this.password = SecretKeyCoder.codingPassword(form.getPassword(), this.salt);
        }
        this.actualName = form.getActualName();
        this.contactNumber = form.getContactNumber();
        this.remark = form.getRemark();
        return this;
    }

    public boolean checkPassword(String password) {
        if (this.getPassword().equals(SecretKeyCoder.codingPassword(password, this.getSalt()))) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 开发阶段初始化root管理员
     *
     * @return
     */
    public Admin addRootAdmin() {
        this.adminName = "admin";
        this.salt = SecretKeyCoder.getSalt(20);
        this.password = SecretKeyCoder.codingPassword("admin", this.salt);
        this.roleId = 1;
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getActualName() {
        return actualName;
    }

    public void setActualName(String actualName) {
        this.actualName = actualName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
