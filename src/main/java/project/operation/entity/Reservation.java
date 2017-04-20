package project.operation.entity;

import project.basic.pojo.OwnerType;
import project.operation.pojo.BookStatus;

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
    //所有人类型
    @Column(nullable = false)
    private OwnerType ownerType;
    //当前持有人id
    @Column(nullable = false)
    private long ownerId;
    //借书用户id
    @Column(nullable = false)
    private long clientId;
    //预约状态（图书状态）
    @Column(nullable = false)
    private BookStatus status = BookStatus.IN_STOCK;
    //预约时间
    @Column(nullable = false)
    private Date createTime = new Date();
    //借书时间
    @Column(nullable = false)
    private Date BorrowedTime = new Date();
    //到期时间
    @Column(nullable = false)
    private Date expireTime = new Date();

    public Reservation() {
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

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBorrowedTime() {
        return BorrowedTime;
    }

    public void setBorrowedTime(Date borrowedTime) {
        BorrowedTime = borrowedTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
