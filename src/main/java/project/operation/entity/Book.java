package project.operation.entity;

import common.Util.SecretKeyCoder;
import project.operation.model.BookForm;
import project.operation.pojo.OwnerType;
import project.operation.pojo.BookStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Errol on 17/4/15.
 */
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /** 基本信息 */
    //书名
    @Column(nullable = false)
    private String name;
    //作者
    @Column(nullable = false)
    private String author;
    //图书分类
    @Column(nullable = false)
    private int classificationId;
    //介绍
    @Column(nullable = false, columnDefinition = "varchar(1000)")
    private String introduction;
    //图片（第一张做封面）
    @Column(nullable = false)
    private String pictures;
    /** 图书状态 及 对应预约记录 */
    //图书状态
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.IN_STOCK;
    //借出时的 预约记录id
    @Column(nullable = false)
    private long reservationId = -1;
    /** 起漂点 */
    //所有者类型，机构或个人
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;
    //所有者id ， 根据 ownerType ，链接到 agencyId 或 clientId
    @Column(nullable = false)
    private long ownerId;
    //起漂点
    @Column(nullable = false)
    private String stackIds = "";
    /** 二维码 */
    //二维码盐值
    @Column(nullable = false)
    private String salt = "";
    //二维码标记
    @Column(nullable = false)
    private String qrCode;

    @Column(nullable = false)
    private Date createTime = new Date();

    public Book() {
    }

    public Book(BookForm form) {
        this.name = form.getName();
        this.author = form.getAuthor();
        this.classificationId = Integer.parseInt(form.getBookClass());
        this.introduction = form.getIntroduction();
        this.pictures = form.getPictures();
        this.status = BookStatus.valueOf(form.getStatus());
        this.ownerType = OwnerType.valueOf(form.getOwnerType());
        this.ownerId = Long.parseLong(form.getOwnerId());
        this.stackIds = form.getStackIds();
//        this.salt = SecretKeyCoder.getSalt(20);
//        this.qrCode = qrCode;
//        this.createTime = createTime;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(int classificationId) {
        this.classificationId = classificationId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public long getReservationId() {
        return reservationId;
    }

    public void setReservationId(long reservationId) {
        this.reservationId = reservationId;
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

    public String getStackIds() {
        return stackIds;
    }

    public void setStackIds(String stackIds) {
        this.stackIds = stackIds;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
