package project.operation.entity;

import project.basic.model.AgencyList;

import javax.persistence.*;

/**
 * Created by Errol on 17/4/15.
 */
@Entity
@Table(name = "stacks")
public class Stacks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //位置
    @Column(nullable = false)
    private String location;
    //开放时间
    @Column(nullable = false)
    private String openTime;

    public Stacks() {
    }
    public Stacks(AgencyList al) {
        this.location = al.getLocation();
        this.openTime = al.getOpenTime();
    }

    /**
     * 开发阶段初始化固定起漂点
     */
    public Stacks(String location, String openTime) {
        this.location = location;
        this.openTime = openTime;
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
