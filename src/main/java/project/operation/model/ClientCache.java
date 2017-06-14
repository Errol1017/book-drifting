package project.operation.model;

import common.Util.Base64Util;
import common.Util.DateUtil;
import project.operation.entity.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Errol on 17/5/5.
 */
public class ClientCache {

    private long id;
    private String avatar;
    private String nickName;
    private String name;
    private String openId;
    private String loginTime;
    private int borrowingSum;
    //所在机构的图书管理员，如果是管理员则为相关机构id，否则为-1
    private int agencyId;
    private List<Long> news = new ArrayList<>();
    //用以保证扫码过程中的连续的两次请求中的第二次请求合法
    private String random = "";

    public ClientCache(Client client) {
        this.id = client.getId();
        this.avatar = Base64Util.img2String(client.getAvatar());
        this.nickName = client.getNickName();
        this.name = client.getName();
        this.openId = client.getOpenId();
        this.loginTime = DateUtil.date2String(client.getLoginTime(), DateUtil.PATTERN_A);
        this.borrowingSum = client.getBorrowingSum();
        this.agencyId = client.isAdmin()?client.getAgencyId():-1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public int getBorrowingSum() {
        return borrowingSum;
    }

    public void setBorrowingSum(int borrowingSum) {
        this.borrowingSum = borrowingSum;
    }

    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    public List<Long> getNews() {
        return news;
    }

    public void setNews(List<Long> news) {
        this.news = news;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }
}
