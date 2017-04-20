package project.operation.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Errol on 17/4/16.
 */
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //图书id
    @Column(nullable = false)
    private long bookId;
    //用户id
    @Column(nullable = false)
    private long clientId;
    //评论内容
    @Column(nullable = false, columnDefinition = "varchar(1000)")
    private String content;
    //评论时间
    @Column(nullable = false)
    private Date createTime = new Date();

    public Comment() {
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

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
