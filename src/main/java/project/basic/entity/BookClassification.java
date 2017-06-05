package project.basic.entity;

import project.basic.model.ClassList;

import javax.persistence.*;

/**
 * Created by Errol on 17/4/15.
 */
@Entity
@Table(name = "book_classification")
public class BookClassification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "smallint unsigned")
    private int id;
    //分类名称
    @Column(nullable = false, unique = true)
    private String name;
    //帅选code
    @Column(nullable = false, unique = true)
    private String code;

    public BookClassification() {
    }

    /**
     * 测试环境下初始化数据用
     */
    public BookClassification(String name) {
        this.name = name;
    }

    public BookClassification(ClassList form) {
        this.name = form.getName();
        this.code = form.getCode();
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
