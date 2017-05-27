package project.open.model;

import common.CRUD.service.ComService;
import project.operation.entity.Book;

/**
 * Created by Errol on 17/5/19.
 */
public class BookListReserve extends BookListParent {

    private String status;
    private String owner;

    public BookListReserve(Book book, ComService comService) {
        super(book, comService);
        this.status = book.getStatus().getName();
    }

    public String getStatus() {
        return status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
