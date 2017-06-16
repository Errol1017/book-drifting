package project.operation.entity;

import project.operation.pojo.OwnerType;
import project.operation.pojo.ReservationStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Errol on 17/4/16.
 */
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //图书id
    @Column(nullable = false)
    private long bookId;
    //当前持有图书的人类型，机构或个人，预约状态中默认 AGENCY
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OwnerType ownerType = OwnerType.AGENCY;
    //当前持有人id（借出方） 委托机构管理则为审核的管理员id
    @Column(nullable = false)
    private long ownerId = -1;
    //借书用户id（借入方）
    @Column(nullable = false)
    private long clientId;
    //预约状态（图书状态）
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.RESERVE;
    //当前可续借一次，续借一次+1，==0不可续借
    @Column(nullable = false)
    private int renew = -1;
    //预约时间
    @Column(nullable = false)
    private Date createTime = new Date();
    //借书时间
    @Column(nullable = false)
    private Date borrowedTime = new Date();
    //归还时间
    @Column(nullable = false)
    private Date recedeTime = new Date();
    //到期时间
    @Column(nullable = false)
    private Date expireTime = new Date();

    public Reservation() {
    }

    public Reservation(long bookId, long clientId) {
        this.bookId = bookId;
        this.clientId = clientId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public int getRenew() {
        return renew;
    }

    public void setRenew(int renew) {
        this.renew = renew;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBorrowedTime() {
        return borrowedTime;
    }

    public void setBorrowedTime(Date borrowedTime) {
        this.borrowedTime = borrowedTime;
    }

    public Date getRecedeTime() {
        return recedeTime;
    }

    public void setRecedeTime(Date recedeTime) {
        this.recedeTime = recedeTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
