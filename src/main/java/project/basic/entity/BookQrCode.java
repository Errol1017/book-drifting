package project.basic.entity;

import javax.persistence.*;

/**
 * Created by Errol on 17/6/3.
 */
@Entity
public class BookQrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String imgName;
    @Column(nullable = false)
    private long bookId = -1;

    public BookQrCode() {
    }

    public BookQrCode(String code, String imgName) {
        this.code = code;
        this.imgName = imgName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
}
