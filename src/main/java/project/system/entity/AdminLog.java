package project.system.entity;

import project.system.model.AdminSession;
import project.system.pojo.OperationTargets;
import project.system.pojo.OperationTypes;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Errol on 16/10/13.
 */
@Entity
@Table(name = "admin_log")
public class AdminLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //管理员id
    @Column(nullable = false, columnDefinition = "smallint unsigned")
    private int adminId;
    //管理员用户名
    @Column(nullable = false)
    private String adminName;
    //操作对象
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationTargets target;
    //操作方式
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationTypes type;
    //操作数据id
    @Column(nullable = false)
    private long targetId;
    //备注
    @Column(nullable = false)
    private String remark;

    //操作时间
    @Column(nullable = false)
    private Date createTime = new Date();

    public AdminLog() {
    }

    public AdminLog(AdminSession adminSession, OperationTargets target, OperationTypes type, long targetId) {
        this.adminId = adminSession.getId();
        this.adminName = adminSession.getName();
        this.target = target;
        this.type = type;
        this.targetId = targetId;
        this.remark = "";
    }

    public AdminLog(AdminSession adminSession, OperationTargets target, OperationTypes type, long targetId, String remark) {
        this.adminId = adminSession.getId();
        this.adminName = adminSession.getName();
        this.target = target;
        this.type = type;
        this.targetId = targetId;
        this.remark = remark;
        this.createTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public OperationTargets getTarget() {
        return target;
    }

    public void setTarget(OperationTargets target) {
        this.target = target;
    }

    public OperationTypes getType() {
        return type;
    }

    public void setType(OperationTypes type) {
        this.type = type;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
