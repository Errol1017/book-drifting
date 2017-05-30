package project.open.model;

import common.Util.Base64Util;
import project.operation.entity.Book;
import project.resource.properties.ServerProperties;

/**
 * Created by Errol on 17/5/19.
 */
public class BookListParent {

    private String id;
    private String name;
    private String face;

    public BookListParent(Book book) {
        this.id = String.valueOf(book.getId());
        this.name = book.getName();
        String[] a = book.getPictures().split(",");
        this.face = Base64Util.img2String(ServerProperties.getInstance().getFileBasePath(), a[0]);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }
}
