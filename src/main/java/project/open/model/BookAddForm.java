package project.open.model;

import project.operation.entity.Book;
import project.operation.pojo.OwnerType;

/**
 * Created by Errol on 17/5/9.
 */
public class BookAddForm {

    private String id;
    private String name;
    private String author;
    private String classId;
    private String intro;
    private String pictures;
//    private String ownerType; //当前只有个人
//    private String ownerId; //当前默认上传者
    private String stackType;
    private String stackId;
    private String qrCode;

    public BookAddForm() {
    }

    public BookAddForm(Book book) {
        this.id = String.valueOf(book.getId());
        this.name = book.getName();
        this.author = book.getAuthor();
        this.classId = String.valueOf(book.getClassificationId());
        this.intro = book.getIntroduction();
        this.pictures = book.getPictures();
        this.stackType = book.getStackType().equals(OwnerType.AGENCY)?"a":"c";
        this.stackId = String.valueOf(book.getStackId());
        this.qrCode = "null";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getStackType() {
        return stackType;
    }

    public void setStackType(String stackType) {
        this.stackType = stackType;
    }

    public String getStackId() {
        return stackId;
    }

    public void setStackId(String stackId) {
        this.stackId = stackId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
