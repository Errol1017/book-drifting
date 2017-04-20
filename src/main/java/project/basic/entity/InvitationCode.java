package project.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Errol on 17/4/15.
 */

@Entity
@Table(name = "invitation_code")
public class InvitationCode {

    @Id
    private String id;
    @Column(nullable = false)
    private long clientId = -1;
//    @Column(nullable = false)
//    private Date createTime = new Date();

    public InvitationCode() {
    }

    public InvitationCode(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
