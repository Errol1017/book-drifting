package project.operation.entity;

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
    private int agencyId;
    /** 个人起漂点 */
    @Column(nullable = false)
    private String individualIds = "";

    //微信id
    //昵称
    //头像
    //性别

    @Column(nullable = false)
    private Date createTime = new Date();
    /** 较频繁变动项 */
    //最近登录时间
    @Column(nullable = false)
    private Date loginTime = new Date();
    //当前借阅的书数量
    @Column(nullable = false)
    private int borrowingNSum;

    public Client() {
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

    public String getIndividualIds() {
        return individualIds;
    }

    public void setIndividualIds(String individualIds) {
        this.individualIds = individualIds;
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

    public int getBorrowingNSum() {
        return borrowingNSum;
    }

    public void setBorrowingNSum(int borrowingNSum) {
        this.borrowingNSum = borrowingNSum;
    }
}
