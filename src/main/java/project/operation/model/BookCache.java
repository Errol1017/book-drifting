package project.operation.model;

import common.Util.Base64Util;
import project.operation.entity.Book;
import project.operation.pojo.BookStatus;
import project.operation.pojo.OwnerType;

/**
 * Created by Errol on 17/5/21.
 */
public class BookCache {

    private String code;
    private long id;
    private String name;
    private String face;
    private String author;
    private long ownerId;
    //图书是否委托机构管理，如果是则为机构id，否为为-1
    private int agencyId;
    private BookStatus status;
    private long holderId = -1;
    //（流程中第一次）扫描了图书的用户id
    private long scannerId = -1;
    private long expireTime = -1;

    public BookCache(Book book) {
        this.code = book.getQrCode();
        this.id = book.getId();
        this.name = book.getName();
        this.face = Base64Util.img2String(book.getFace());
        this.author = book.getAuthor();
        this.ownerId = book.getOwnerId();
        this.agencyId = book.getStackType().equals(OwnerType.AGENCY) ? (int) (book.getStackId()) : -1;
        this.status = book.getStatus();
    }
    public void modify(Book book){
//        this.code = book.getQrCode();
//        this.id = book.getId();
        this.name = book.getName();
        this.face = Base64Util.img2String(book.getFace());
        this.author = book.getAuthor();
        this.ownerId = book.getOwnerId();
        this.agencyId = book.getStackType().equals(OwnerType.AGENCY) ? (int) (book.getStackId()) : -1;
        this.status = book.getStatus();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public long getHolderId() {
        return holderId;
    }

    public void setHolderId(long holderId) {
        this.holderId = holderId;
    }

    public long getScannerId() {
        return scannerId;
    }

    public void setScannerId(long scannerId) {
        this.scannerId = scannerId;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
