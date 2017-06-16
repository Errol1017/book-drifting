package project.operation.entity;

import common.FileProcessor.FileManager;
import common.FileProcessor.image.ImgUtil;
import project.open.model.BookAddForm;
import project.operation.model.BookForm;
import project.operation.pojo.OwnerType;
import project.operation.pojo.BookStatus;
import project.resource.pojo.UploadFolders;

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
    /**
     * 基本信息
     */
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
    private String face;
    @Column(nullable = false)
    private String pictures;
    /**
     * 所有人和起漂点
     * 图书所有权属于上传图书的 个人 或 机构
     * 个人可以选择自己管理图书的流转（个人起漂点）或者委托机构进行管理（机构起漂点）
     * 机构则只能选择 机构起漂点
     */
    //所有者类型，机构或个人
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;
    //所有者id ， 根据 ownerType ，链接到 agencyId 或 clientId
    @Column(nullable = false)
    private long ownerId;
    //起漂点 -- 只允许一个起漂点，还是可以允许多个，主要在个人管理的情况
//    @Column(nullable = false)
//    private String stackIds = "";
    //管理者类型，如果是个人类型则 stackId 就是 stacks 表 id，机构类型则 stackId 为 agency 表 id
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OwnerType stackType;
    @Column(nullable = false)
    private long stackId = -1;
    /**
     * 二维码
     */
    //二维码盐值
    @Column(nullable = false)
    private String salt = "";
    //二维码标记
    @Column(nullable = false)
    private String qrCode;

    @Column(nullable = false)
    private Date createTime = new Date();

    /** 频分变动项 */
    /**
     * 图书状态 及 对应预约记录
     */
    //图书状态
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.UNPREPARED;
    //借出时的 预约记录id
    @Column(nullable = false)
    private long reservationId = -1;

    public Book() {
    }

    //后台添加图书
//    public Book(BookForm form) {
//        this.name = form.getName();
//        this.author = form.getAuthor();
//        this.classificationId = Integer.parseInt(form.getBookClass());
//        this.introduction = form.getIntroduction();
//        this.pictures = form.getPictures();
//        this.status = BookStatus.valueOf(form.getStatus());
//        this.ownerType = OwnerType.valueOf(form.getOwnerType());
//        this.ownerId = Long.parseLong(form.getOwner());
////        this.stackIds = form.getStackId();
//        this.stackType = OwnerType.AGENCY;
////        this.stackId = Long.parseLong(form.getStackId());
////        this.salt = SecretKeyCoder.getSalt(20);
////        this.qrCode = qrCode;
////        this.createTime = createTime;
//    }

    //用户上传图书
    public Book(BookAddForm form, long ownerId) {
        this.name = form.getName();
        this.author = form.getAuthor();
        this.classificationId = Integer.parseInt(form.getClassId());
        this.introduction = form.getIntro();
        String[] pics = form.getPictures().split(",");
        String f = "";
        StringBuffer pictures = new StringBuffer();
        for (String s : pics) {
            String c = ImgUtil.cut(s, 3, 4);
            if (f.equals("")) {
                f = c;
            }
            pictures.append(FileManager.save(c, UploadFolders.img) + ",");
        }
        pictures.deleteCharAt(pictures.length() - 1);
        this.pictures = pictures.toString();
        this.face = FileManager.save(ImgUtil.scale(f, 132), UploadFolders.img);
        this.ownerType = OwnerType.INDIVIDUAL; //目前图书所有人只有个人
        this.ownerId = ownerId;
        this.stackType = form.getStackType().equals("a") ? OwnerType.AGENCY : OwnerType.INDIVIDUAL;
        this.stackId = Long.parseLong(form.getStackId());
//        this.salt = SecretKeyCoder.getSalt(10);
        this.salt = "";
        this.qrCode = form.getQrCode();
        if (!form.getStackType().equals("a")) {
            this.status = BookStatus.IN_STOCK;
        }
    }

    //用户编辑图书
    public void modify(BookAddForm form) {
        this.name = form.getName();
        this.author = form.getAuthor();
        this.classificationId = Integer.parseInt(form.getClassId());
        this.introduction = form.getIntro();
        if (!this.pictures.equals(form.getPictures())) {
            String[] pics = form.getPictures().split(",");
            String f = "";
            StringBuffer pictures = new StringBuffer();
            for (String s : pics) {
                String c = ImgUtil.cut(s, 3, 4);
                if (f.equals("")) {
                    f = c;
                }
                pictures.append(FileManager.save(c, UploadFolders.img) + ",");
            }
            pictures.deleteCharAt(pictures.length() - 1);
            this.pictures = pictures.toString();
            this.face = FileManager.save(ImgUtil.scale(f, 132), UploadFolders.img);
        }
        this.stackType = form.getStackType().equals("a") ? OwnerType.AGENCY : OwnerType.INDIVIDUAL;
        this.stackId = Long.parseLong(form.getStackId());
//        this.qrCode = form.getQrCode();
        if (this.status.equals(BookStatus.UNPREPARED) && this.stackType.equals(OwnerType.INDIVIDUAL)) {
            this.status = BookStatus.IN_STOCK;
        }
        if (this.status.equals(BookStatus.IN_STOCK) && this.stackType.equals(OwnerType.AGENCY)) {
            this.status = BookStatus.UNPREPARED;
        }
    }

    //后台修改图书
    public void modify(BookForm form) {
        this.name = form.getName();
        this.author = form.getAuthor();
        this.classificationId = Integer.parseInt(form.getBookClass());
        this.introduction = form.getIntroduction();
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

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
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

//    public String getStackId() {
//        return stackIds;
//    }
//
//    public void setStackIds(String stackIds) {
//        this.stackIds = stackIds;
//    }

    public OwnerType getStackType() {
        return stackType;
    }

    public void setStackType(OwnerType stackType) {
        this.stackType = stackType;
    }

    public long getStackId() {
        return stackId;
    }

    public void setStackId(long stackId) {
        this.stackId = stackId;
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
