package project.basic.entity;

import project.basic.model.AgencyList;

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
    //固定起漂点ID
    @Column(nullable = false)
    private long stackId = -1;
    /** 是否加 联系人 列表 */
//    @Column(nullable = false)
//    private String clientIds = "";

    public Agency() {
    }
    public Agency(AgencyList al, long stackId) {
        this.name = al.getName();
        this.stackId = stackId;
    }

    /**
     * 开发阶段初始化单位信息
     */
    public Agency(String name, long stackId) {
        this.name = name;
        this.stackId = stackId;
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

    public long getStackId() {
        return stackId;
    }

    public void setStackId(long stackId) {
        this.stackId = stackId;
    }
}
