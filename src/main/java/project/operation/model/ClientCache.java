package project.operation.model;

import project.operation.entity.Client;

import java.util.Date;

/**
 * Created by Errol on 17/5/5.
 */
public class ClientCache {

    private String openId;
    private Date loginTime;
    private int borrowingSum;

    public ClientCache(Client client) {
        this.openId = client.getOpenId();
        this.loginTime = client.getLoginTime();
        this.borrowingSum = client.getBorrowingSum();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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
