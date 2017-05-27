package project.open.model;

import common.CRUD.service.ComService;
import common.Util.Base64Util;
import project.operation.entity.Book;

/**
 * Created by Errol on 17/5/19.
 */
public class BookListParent {

    private String id;
    private String name;
    private String face;

    public BookListParent(Book book, ComService comService) {
        this.id = String.valueOf(book.getId());
        this.name = book.getName();
        String[] a = book.getPictures().split(",");
        this.face = Base64Util.img2String(comService.getFileBathPath(), a[0]);
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
