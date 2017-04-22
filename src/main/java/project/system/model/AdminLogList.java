package project.system.model;

import project.system.entity.AdminLog;
import common.Util.DateUtil;

/**
 * Created by Errol on 16/10/14.
 */
public class AdminLogList {

    private String id;
    private String adminName;
    private String type;
    private String target;
    private String targetId;
    private String remark;
    private String createTime;

    public AdminLogList(AdminLog log) {
        this.id = String.valueOf(log.getId());
        this.adminName = log.getAdminName();
        this.type = log.getType().getName();
        this.target = log.getTarget().getName();
        this.targetId = String.valueOf(log.getTargetId());
        this.remark = log.getRemark();
        this.createTime = DateUtil.date2String(log.getCreateTime(), DateUtil.PATTERN_G);
    }

    public String getId() {
        return id;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getRemark() {
        return remark;
    }

    public String getCreateTime() {
        return createTime;
    }
}
