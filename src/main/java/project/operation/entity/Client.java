package project.operation.entity;

import common.pojo.Gender;
import project.open.model.UserVerifyForm;
import project.operation.model.ClientForm;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Errol on 17/4/15.
 */
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //姓名
    @Column(nullable = false)
    private String name;
    //电话
    @Column(nullable = false)
    private String mobile;
    //身份证号
    @Column(nullable = false)
    private String identityNumber;
    //单位
    @Column(nullable = false, columnDefinition = "smallint unsigned")
    private int agencyId = -1;
    //是否为所在单位图书管理员
    @Column(nullable = false)
    private boolean isAdmin = false;
    /**
     * 个人起漂点
     */
    //是否建立默认起漂点？申请图书流转时可以显示持有用户的默认起漂点
    @Column(nullable = false)
    private String stackIds = "";
    /**
     * 微信身份相关
     */
    //微信openId
    @Column(nullable = false, unique = true)
    private String openId = "";
    //昵称
    @Column(nullable = false)
    private String nickName = "";
    //头像
    @Column(nullable = false)
    private String portrait = "avatar/avatar.png";
    @Column(nullable = false)
    private String avatar = "avatar/avatar.png";
    //性别
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Gender gender = Gender.UNKNOWN;

    @Column(nullable = false)
    private Date createTime = new Date();
    /**
     * 较频繁变动项
     */
    //最近登录时间
    @Column(nullable = false)
    private Date loginTime = new Date();
    //当前借阅的书数量 ？ 是否添加预约数量
    @Column(nullable = false)
    private int borrowingSum = 0;

    public Client() {
    }

    public Client(ClientForm form) {
        this.name = form.getName();
        this.mobile = form.getMobile();
        this.identityNumber = form.getIdentityNumber();
        if (!form.getAgencyId().equals("")) {
            this.agencyId = Integer.parseInt(form.getAgencyId());
        }
        this.stackIds = form.getStackIds();
        this.nickName = form.getName();
    }

    public Client(UserVerifyForm form) {
        this.name = form.getName();
        this.mobile = form.getMobile();
        this.identityNumber = form.getIdNum();
        this.agencyId = form.getAgencyId();
        this.nickName = form.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getStackIds() {
        return stackIds;
    }

    public void setStackIds(String stackIds) {
        this.stackIds = stackIds;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public int getBorrowingSum() {
        return borrowingSum;
    }

    public void setBorrowingSum(int borrowingSum) {
        this.borrowingSum = borrowingSum;
    }
}
