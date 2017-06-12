package project.operation.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Errol on 17/6/6.
 */
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //发送人，默认-1为系统
    @Column(nullable = false)
    private long senderId = -1;
    //接收用户id
    @Column(nullable = false)
    private long clientId;
    //消息内容
    @Column(nullable = false)
    private String content;
    //发送时间
    @Column(nullable = false)
    private Date createTime = new Date();

    public Message() {
    }

    public Message(long senderId, long clientId, String content) {
        this.senderId = senderId;
        this.clientId = clientId;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
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
