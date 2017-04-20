package project.basic.entity;

import javax.persistence.*;

/**
 * Created by Errol on 17/4/15.
 */
@Entity
@Table(name = "agency")
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "smallint unsigned")
    private int id;
    //机构名称
    @Column(nullable = false)
    private String name;
    //机构地址
    @Column(nullable = false)
    private String location;
    //开放时间
    @Column(nullable = false)
    private String openTime;
    /** 是否加 联系人 列表 */
//    @Column(nullable = false)
//    private String clientIds = "";

    public Agency() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
