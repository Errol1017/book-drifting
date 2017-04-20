package project.basic.entity;

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
    @Column(nullable = false)
    private String name;

    public BookClassification() {
    }

    public BookClassification(String name) {
        this.name = name;
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
}
