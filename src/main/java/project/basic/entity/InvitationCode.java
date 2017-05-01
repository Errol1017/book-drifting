package project.basic.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Errol on 17/4/15.
 */

@Entity
@Table(name = "invitation_code")
public class InvitationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private long clientId = -1;
//    @Column(nullable = false)
//    private Date createTime = new Date();

    public InvitationCode() {
    }

    public InvitationCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
//
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
}
