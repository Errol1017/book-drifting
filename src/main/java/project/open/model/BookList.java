package project.open.model;

import common.CRUD.service.ComService;
import common.Util.Base64Util;
import project.operation.entity.Book;

/**
 * Created by Errol on 17/5/9.
 */
public class BookList {

    private String id;
    private String name;
    private String author;
    private String face;
    private String status;

    public BookList(Book book, ComService comService) {
        this.id = String.valueOf(book.getId());
        this.name = book.getName();
        this.author = book.getAuthor();
        String[] a = book.getPictures().split(",");
        this.face = Base64Util.img2String(comService.getFileBathPath(), a[0]);
        this.status = book.getStatus().getName();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getFace() {
        return face;
    }

    public String getStatus() {
        return status;
    }
}
