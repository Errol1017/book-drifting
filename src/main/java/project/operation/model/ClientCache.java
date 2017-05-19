package project.operation.model;

import common.Util.DateUtil;
import project.operation.entity.Client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Errol on 17/5/5.
 */
public class ClientCache {

    private long id;
    private String nickName;
    private String openId;
    private String loginTime;
    private int borrowingSum;
    private List<Long> news = new ArrayList<>();

    public ClientCache(Client client) {
        this.id = client.getId();
        this.nickName = client.getNickName();
        this.openId = client.getOpenId();
        this.loginTime = DateUtil.date2String(client.getLoginTime(), DateUtil.PATTERN_A);
        this.borrowingSum = client.getBorrowingSum();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public List<Long> getNews() {
        return news;
    }

    public void setNews(List<Long> news) {
        this.news = news;
    }
}
