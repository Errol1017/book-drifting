package project.open.model;

import common.CRUD.service.ComService;
import project.navigator.service.CacheManager;
import project.operation.entity.Book;

/**
 * Created by Errol on 17/5/19.
 */
public class BookListReserve extends BookListParent {

    private String status;
    private String owner;
    private String rid;

    public BookListReserve(Book book, String rid, CacheManager cacheManager, ComService comService) {
        super(cacheManager.getBookCache(book.getId()));
        this.rid = rid;
        this.status = book.getStatus().getName();
        this.owner = this.getOwnerString(book, cacheManager, comService);
    }
//    public BookListReserve(Book book) {
//        super(book);
//        this.status = book.getStatus().getName();
//        this.rid = "";
//    }

    public String getRid() {
        return rid;
    }

    public String getStatus() {
        return status;
    }

    public String getOwner() {
        return owner;
    }

//    public void setOwner(String owner) {
//        this.owner = owner;
//    }
}
