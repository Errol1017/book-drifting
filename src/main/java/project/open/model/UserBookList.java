package project.open.model;

import common.CRUD.service.ComService;
import common.ServerAdvice.util.LogUtil;
import project.navigator.service.CacheManager;
import project.operation.entity.Book;
import project.operation.pojo.BookStatus;
import project.operation.pojo.OwnerType;

/**
 * Created by Errol on 17/6/11.
 */
public class UserBookList extends BookListParent {

    private String status;
    private String owner;
    private String editable;
    private String releasable;

    public UserBookList(Book book, CacheManager cacheManager, ComService comService) {
        super(cacheManager.getBookCache(book.getId()));
        BookStatus status = book.getStatus();
        this.status = status.getName();
        if (status.equals(BookStatus.RELEASED) || status.equals(BookStatus.FROZEN)) {
            this.releasable = "0";
        } else {
            this.releasable = "1";
        }
        LogUtil.debug(status.toString());
        LogUtil.debug(book.getStackType().toString());
        LogUtil.debug(String.valueOf(book.getStackType().equals(OwnerType.INDIVIDUAL) && status.equals(BookStatus.IN_STOCK)));
        if (status.equals(BookStatus.UNPREPARED) || status.equals(BookStatus.FROZEN) || (book.getStackType().equals(OwnerType.INDIVIDUAL) && status.equals(BookStatus.IN_STOCK))) {
            this.editable = "1";
        } else {
            this.editable = "0";
        }
        this.owner = this.getOwnerString(book, cacheManager, comService);
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

    public String getEditable() {
        return editable;
    }

    public String getReleasable() {
        return releasable;
    }
}
