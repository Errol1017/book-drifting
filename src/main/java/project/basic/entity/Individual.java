package project.basic.entity;

import javax.persistence.*;

/**
 * Created by Errol on 17/4/15.
 */
@Entity
@Table(name = "individual")
public class Individual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //位置
    @Column(nullable = false)
    private String location;
    //开放时间
    @Column(nullable = false)
    private String openTime;

    public Individual() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }
}
